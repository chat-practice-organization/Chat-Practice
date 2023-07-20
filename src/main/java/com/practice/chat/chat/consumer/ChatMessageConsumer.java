package com.practice.chat.chat.consumer;

import com.practice.chat.chat.domain.ChatMessage;
import com.practice.chat.chat.service.SendMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {


    public static final AtomicInteger count = new AtomicInteger();
    private final ObjectMapper objectMapper;
    private final SendMessageService sendMessageService;


    @KafkaListener(topics = "${kafka.topic.chat.receive}", containerFactory = "kafkaBatchListenerContainerFactory")
    public void consume(List<String> messages) {
        log.info("batch size:" + messages.size());
        messages.forEach(message -> {
            ChatMessage chatMessage = null;
            try {
                chatMessage = objectMapper.readValue(message, ChatMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            sendMessageService.sendChatMessage(chatMessage);

        });
        count.addAndGet(messages.size());
    }
}
