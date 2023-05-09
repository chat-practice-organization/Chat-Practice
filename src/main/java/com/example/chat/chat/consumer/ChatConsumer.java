package com.example.chat.chat.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@Slf4j
public class ChatConsumer {

    @RabbitListener(queues = "chat")
    public void receiveChat(String chat){

      log.info("chat message : "+chat);
    }
}
