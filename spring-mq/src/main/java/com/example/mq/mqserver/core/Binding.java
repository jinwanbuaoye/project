package com.example.mq.mqserver.core;

/**
 * 队列和交换机的关系
 */
public class Binding {
    private String exchangeName; //交换机名字
    private String queueName; //队列名字
    private String bindingKey; //只在交换机类型为 TOPIC 时才有效. 用于和消息中的 routingKey 进行匹配.

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }

    public String getBindingKey() {
        return bindingKey;
    }

    public void setBindingKey(String bindingKey) {
        this.bindingKey = bindingKey;
    }
}
