package com.example.mq.mqserver.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.util.HashMap;
import java.util.Map;

/**
 * 交换机
 */
public class Exchange {
    private String name; //交换机的名字. 相当于交换机的身份标识.

    private ExchangeType type = ExchangeType.DIRECT; //type : 交换机的类型. 三种取值, DIRECT, FANOUT, TOPIC.
    private boolean durable = false; //交换机是否要持久化存储. true 为持久化, false 不持久化.
    private boolean autoDelete = false; //使用完毕后是否自动删除. 预留字段, 暂时未使用.
    private Map<String, Object> arguments = new HashMap<>(); //交换机的其他参数属性. 预留字段, 暂时未使用.

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ExchangeType getType() {
        return type;
    }

    public void setType(ExchangeType type) {
        this.type = type;
    }

    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }

    public boolean isAutoDelete() {
        return autoDelete;
    }

    public void setAutoDelete(boolean autoDelete) {
        this.autoDelete = autoDelete;
    }

    // 这里的 get set 用于和数据库交互使用.
    public String getArguments() {
        // 是把当前的 arguments 参数, 从 Map 转成 String (JSON)
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(arguments);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        // 如果代码真异常了, 返回一个空的 json 字符串就 ok
        return "{}";
    }

    // 这个方法, 是从数据库读数据之后, 构造 Exchange 对象, 会自动调用到
    public void setArguments(String argumentsJson) {
        // 把参数中的 argumentsJson 按照 JSON 格式解析, 转成
        // 上述的 Map 对象
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            this.arguments = objectMapper.readValue(argumentsJson, new TypeReference<HashMap<String, Object>>() {});
            //第二个参数，用来描述当前json字符串，要转成的java 对象是啥类型的.
            //如果是个简单类型，直接使用对应类型的类对象即可.
            //如果是集合类这样的复杂类型，可以使用TypeReference 匿名内部类对象，来描述复杂类型的具体信息.（通过泛型参数来描述的）
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    // 在这里针对 arguments, 再提供一组 getter setter , 用来去更方便的获取/设置这里的键值对.
    // 这一组在 java 代码内部使用 (比如测试的时候)
    public Object getArguments(String key) {
        return arguments.get(key);
    }

    public void setArguments(String key, Object value) {
        arguments.put(key, value);
    }

    public void setArguments(Map<String, Object> arguments) {
        this.arguments = arguments;
    }
}
