package com.ityu.studystreamrmq.rabbitmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class TestSendMessage implements RabbitTemplate.ConfirmCallback, RabbitTemplate.ReturnCallback {


    private final RabbitTemplate rabbitTemplate;

    public TestSendMessage(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
        rabbitTemplate.setConfirmCallback(this::confirm); //rabbitTemplate如果为单例的话，那回调就是最后设置的内容
        rabbitTemplate.setReturnCallback(this::returnedMessage);
        rabbitTemplate.setMandatory(true);
    }

    public void sendMsg(String exchange, String routkey, Object content) {
        CorrelationData correlationId = new CorrelationData(UUID.randomUUID().toString());
        rabbitTemplate.convertAndSend(exchange, routkey, content, correlationId);

    }

    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {
        log.info(" 消息确认的id： " + correlationData);
        if (ack) {
            log.info("消息发送成功");
            //发送成功 删除本地数据库存的消息
        } else {
            log.info("消息发送失败：id " + correlationData + "消息发送失败的原因" + cause);
            // 根据本地消息的状态为失败，可以用定时任务去处理数据

        }
    }

    @Override
    public void returnedMessage(Message message, int replyCode, String replyText, String exchange, String routingKey) {
        log.info("returnedMessage [消息从交换机到队列失败]  message：" + message);
    }

}
