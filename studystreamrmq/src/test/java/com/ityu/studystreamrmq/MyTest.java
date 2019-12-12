package com.ityu.studystreamrmq;


import com.ityu.studystreamrmq.rabbitmq.QueueEnum;
import com.ityu.studystreamrmq.rabbitmq.TestSendMessage;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MyTest {

    @Autowired
    private TestSendMessage amqpTemplate;
    @Autowired
    private RabbitTemplate amqpTemplate2;

    @Test
    public void sendMsg() {
        amqpTemplate.sendMsg(QueueEnum.QUEUE_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_ORDER_CANCEL.getName(), "你好啊不错哦");
    }

    @Test
    public void sendMsg2() {
        System.out.println(System.currentTimeMillis());
        amqpTemplate2.convertAndSend(QueueEnum.QUEUE_TTL_ORDER_CANCEL.getExchange(), QueueEnum.QUEUE_TTL_ORDER_CANCEL.getRouteKey(), "我是延迟消息你能发现不50000", message -> {
            message.getMessageProperties().setExpiration("500000");
            return message;
        });
    }


}
