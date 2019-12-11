package com.ityu.studystreamrmq.rabbitmq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RabbitListener(queues = "ityu.order.cancel")
@Slf4j
public class Customer1 {

    @RabbitHandler
    public void handle(String message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) throws IOException {
        System.out.println(System.currentTimeMillis());
        System.out.println(message);
//        try {
//            /**
//             * 防止重复消费，可以根据传过来的唯一ID先判断缓存数据库中是否有数据
//             * 1、有数据则不消费，直接应答处理
//             * 2、缓存没有数据，则进行消费处理数据，处理完后手动应答
//             * 3、如果消息 处理异常则，可以存入数据库中，手动处理（可以增加短信和邮件提醒功能）
//             */
//             channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
//        } catch (IOException e) {
//            e.printStackTrace();
//            channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, true);
//        }


    }
}
