package com.example.mq.mqserver.core;

import java.io.Serializable;

public class BasicProperties implements Serializable {
    private String messageId; // 消息的唯一 id. 使用 uuid算法 表示.（用来三次唯一id的算法)

    private String routingKey;
    // 是一个消息上带有的内容, 和 bindingKey 做匹配.
    // 如果当前的交换机类型是 DIRECT, 此时 routingKey 就表示要转发的队列名.
    // 如果当前的交换机类型是 FANOUT, 此时 routingKey 无意义(不使用).
    // 如果当前的交换机类型是 TOPIC, 此时 routingKey 就要和 bindingKey 做匹配. 符合要求的才能转发给对应队列.

    private int deliveryMode = 1; // 1 表示消息非持久化. 2 表示消息持久化

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getRoutingKey() {
        return routingKey;
    }

    public void setRoutingKey(String routingKey) {
        this.routingKey = routingKey;
    }

    public int getDeliveryMode() {
        return deliveryMode;
    }

    public void setDeliveryMode(int deliveryMode) {
        this.deliveryMode = deliveryMode;
    }
}
