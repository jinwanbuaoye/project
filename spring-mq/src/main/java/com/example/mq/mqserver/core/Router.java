package com.example.mq.mqserver.core;

import com.example.mq.common.MqException;

/*
 * 使用这个类, 来实现交换机的转发规则.
 * 同时也借助这个类验证 bindingKey 是否合法.
 */
public class Router {
    // bindingKey 的构造规则:
    // 1. 数字, 字母, 下划线
    // 2. 使用 . 分割成若干部分
    // 3. 允许存在 * 和 # 作为通配符. 但是通配符只能作为独立的分段.
    public boolean checkBindingKey(String bindingKey) {
        if (bindingKey.length() == 0) {
            // 空字符串, 也是合法情况. 比如在使用 direct / fanout 交换机的时候, bindingKey 是用不上的.
            return true;
        }
        // 检查字符串中不能存在非法字符
        for (int i = 0; i < bindingKey.length(); i++) {
            char ch = bindingKey.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == '_' || ch == '.' || ch == '*' || ch == '#') {
                continue;
            }
            return false;
        }
        // 检查 * 或者 # 是否是独立的部分.
        // aaa.*.bbb 合法情况;  aaa.a*.bbb 非法情况.
        String[] words = bindingKey.split("\\.");
        for (String word : words) {
            // 检查 word 长度 > 1 并且包含了 * 或者 # , 就是非法的格式了.
            if (word.length() > 1 && (word.contains("*") || word.contains("#"))) {
                return false;
            }
        }
        // 约定一下, 通配符之间的相邻关系(人为(俺)约定的).
        // 为啥这么约定? 因为前三种相邻的时候, 实现匹配的逻辑会非常繁琐, 同时功能性提升不大~~
        // 1. aaa.#.#.bbb    => 非法
        // 2. aaa.#.*.bbb    => 非法
        // 3. aaa.*.#.bbb    => 非法
        // 4. aaa.*.*.bbb    => 合法
        for (int i = 0; i < words.length - 1; i++) {
            // 连续两个 ##
            if (words[i].equals("#") && words[i + 1].equals("#")) {
                return false;
            }
            // # 连着 *
            if (words[i].equals("#") && words[i + 1].equals("*")) {
                return false;
            }
            // * 连着 #
            if (words[i].equals("*") && words[i + 1].equals("#")) {
                return false;
            }
        }
        return true;
    }

    // routingKey 的构造规则:
    // 1. 数字, 字母, 下划线
    // 2. 使用 . 分割成若干部分
    public boolean checkRoutingKey(String routingKey) {
        if (routingKey.length() == 0) {
            // 空字符串. 合法的情况. 比如在使用 fanout 交换机的时候, routingKey 用不上, 就可以设为 ""
            return true;
        }
        for (int i = 0; i < routingKey.length(); i++) {
            char ch = routingKey.charAt(i);
            // 判定该字符是否是大写字母
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            // 判定该字母是否是小写字母
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            // 判定该字母是否是阿拉伯数字
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            // 判定是否是 _ 或者 .
            if (ch == '_' || ch == '.') {
                continue;
            }
            // 该字符, 不是上述任何一种合法情况, 就直接返回 false
            return false;
        }
        // 把每个字符都检查过, 没有遇到非法情况. 此时直接返回 true
        return true;
    }

    // 这个方法用来判定该消息是否可以转发给这个绑定对应的队列.
    public boolean route(ExchangeType exchangeType, Binding binding, Message message) throws MqException {
        // 根据不同的 exchangeType 使用不同的判定转发规则.
        if (exchangeType == ExchangeType.FANOUT) {
            // 如果是 FANOUT 类型, 则该交换机上绑定的所有队列都需要转发
            return true;
        } else if (exchangeType == ExchangeType.TOPIC) {
            // 如果是 TOPIC 主题交换机, 规则就要更复杂一些.
            return routeTopic(binding, message);
        } else {
            // 其他情况是不应该存在的.
            throw new MqException("[Router] 交换机类型非法! exchangeType=" + exchangeType);
        }
    }

    // [测试用例]
    // binding key          routing key         result
    // aaa                  aaa                 true
    // aaa.bbb              aaa.bbb             true
    // aaa.bbb              aaa.bbb.ccc         false
    // aaa.bbb              aaa.ccc             false
    // aaa.bbb.ccc          aaa.bbb.ccc         true
    // aaa.*                aaa.bbb             true
    // aaa.*.bbb            aaa.bbb.ccc         false
    // *.aaa.bbb            aaa.bbb             false
    // #                    aaa.bbb.ccc         true
    // aaa.#                aaa.bbb             true
    // aaa.#                aaa.bbb.ccc         true
    // aaa.#.ccc            aaa.ccc             true
    // aaa.#.ccc            aaa.bbb.ccc         true
    // aaa.#.ccc            aaa.aaa.bbb.ccc     true
    // #.ccc                ccc                 true
    // #.ccc                aaa.bbb.ccc         true
    private boolean routeTopicOld(Binding binding, Message message) {
        // 先把这两个 key 进行切分
        String[] bindingTokens = binding.getBindingKey().split("\\.");
        String[] routingTokens = message.getRoutingKey().split("\\.");

        // 引入两个下标, 指向上述两个数组. 初始情况下都为 0
        int bindingIndex = 0;
        int routingIndex = 0;
        // 此处使用 while 更合适, 每次循环, 下标不一定就是 + 1, 不适合使用 for
        while (bindingIndex < bindingTokens.length && routingIndex < routingTokens.length) {
            if (bindingTokens[bindingIndex].equals("*")) {
                // [情况二] 如果遇到 * , 直接进入下一轮. * 可以匹配到任意一个部分!!
                bindingIndex++;
                routingIndex++;
                continue;
            } else if (bindingTokens[bindingIndex].equals("#")) {
                // 如果遇到 #, 需要先看看有没有下一个位置.
                bindingIndex++;
                if (bindingIndex == bindingTokens.length) {
                    // [情况三] 该 # 后面没东西了, 说明此时一定能匹配成功了!
                    return true;
                }
                // [情况四] # 后面还有东西, 拿着这个内容, 去 routingKey 中往后找, 找到对应的位置.
                // findNextMatch 这个方法用来查找该部分在 routingKey 的位置. 返回该下标. 没找到, 就返回 -1
                routingIndex = findNextMatch(routingTokens, routingIndex, bindingTokens[bindingIndex]);
                if (routingIndex == -1) {
                    // 没找到匹配的结果. 匹配失败
                    return false;
                }
                // 找到的匹配的情况, 继续往后匹配.
                bindingIndex++;
                routingIndex++;
            } else {
                // [情况一] 如果遇到普通字符串, 要求两边的内容是一样的.
                if (!bindingTokens[bindingIndex].equals(routingTokens[routingIndex])) {
                    return false;
                }
                bindingIndex++;
                routingIndex++;
            }
        }
        // [情况五] 判定是否是双方同时到达末尾
        // 比如 aaa.bbb.ccc  和  aaa.bbb 是要匹配失败的.
        if (bindingIndex == bindingTokens.length && routingIndex == routingTokens.length) {
            return true;
        }
        return false;
    }

    private int findNextMatch(String[] routingTokens, int routingIndex, String bindingToken) {
        for (int i = routingIndex; i < routingTokens.length; i++) {
            if (routingTokens[i].equals(bindingToken)) {
                return i;
            }
        }
        return -1;
    }

    // 通过 DP 的方式重新实现这个方法. 实现思路参考 "动态规划精品课" 里的 "44.两个数组的 dp 问题_通配符匹配_Java"
    private boolean routeTopic(Binding binding, Message message) {
        // 按照 . 来切分 binding key 和 routing key

        // 无通配符
        String[] routingTokens = message.getRoutingKey().split("\\.");
        // 有通配符
        String[] bindingTokens = binding.getBindingKey().split("\\.");
        int m = routingTokens.length;
        int n = bindingTokens.length;

        // 1. 初始化 dp 表. 由于要考虑空串, dp 表的长和宽都要 + 1
        // dp[i][j] 表示的含义是 bindingTokens 中的 [0, j] 能否和 routingTokens 中的 [0, i] 匹配.
        boolean[][] dp = new boolean[m + 1][n + 1];
        // 空的 bindingKey 和 空的 routingKey 可以匹配
        dp[0][0] = true;
        // 如果 routingKey 为空, bindingKey 只有连续为 # 的时候, 才能匹配.
        for (int j = 1; j <= n; j++) {
            if (bindingTokens[j - 1].equals("#")) {
                dp[0][j] = true;
            } else {
                break;
            }
        }
        // 2. 遍历所有情况
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (bindingTokens[j - 1].equals("#")) {
                    // 这块的状态转移方程推导过程很复杂. 参考算法视频讲解
                    dp[i][j] = dp[i - 1][j] || dp[i][j - 1];
                } else if (bindingTokens[j - 1].equals("*")) {
                    // 如果 bindingTokens j 位置为 *, 那么 bindingTokens j - 1 位置和 routingKey i - 1 位置匹配即可.
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    // 如果 bindingTokens j 位置为普通字符串, 那么要求 bindingTokens j - 1 位置 和 routingKey i - 1
                    // 位置匹配
                    // 并且 bindingTokens j 位置和 routingKey i 位置相同, 才认为是匹配
                    if (bindingTokens[j - 1].equals(routingTokens[i - 1])) {
                        dp[i][j] = dp[i - 1][j - 1];
                    } else {
                        dp[i][j] = false;
                    }
                }
            }
        }
        // 3. 处理返回值, 直接返回 dp 表的最后一个位置
        return dp[m][n];
    }

    // 需要考虑通配符, 复杂一些
    public boolean checkBindingKeyValid(String bindingKey) {
        // 1. 允许是空字符串
        // 2. 数字字母下划线构成
        // 3. 可以包含通配符
        // 4. # 不能连续出现.
        // 5. # 和 * 不能相邻
        if (bindingKey.length() == 0) {
            return true;
        }
        // 先判定基础构成
        for (int i = 0; i < bindingKey.length(); i++) {
            char ch = bindingKey.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == '.' || ch == '_' || ch == '*' || ch == '#') {
                continue;
            }
            return false;
        }
        // 再判定每个词的情况
        // 比如 aaa.a*a 这种应该视为非法.
        String[] words = bindingKey.split("\\.");
        for (String word : words) {
            if (word.length() > 1 && (word.contains("*") || word.contains("#"))) {
                return false;
            }
        }
        // 再判定相邻词的情况
        for (int i = 0; i < words.length - 1; i++) {
            // 连续两个 ##
            if (words[i].equals("#") && words[i + 1].equals("#")) {
                return false;
            }
            // # 连着 *
            if (words[i].equals("#") && words[i + 1].equals("*")) {
                return false;
            }
            // * 连着 #
            if (words[i].equals("*") && words[i + 1].equals("#")) {
                return false;
            }
        }
        return true;
    }

    // 不包含通配符, 规则更简单.
    public boolean checkRoutingKeyValid(String routingKey) {
        if (routingKey.length() == 0) {
            return true;
        }
        // 数字字母下划线构成
        for (int i = 0; i < routingKey.length(); i++) {
            char ch = routingKey.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == '_' || ch == '.') {
                continue;
            }
            return false;
        }
        return true;
    }
}
