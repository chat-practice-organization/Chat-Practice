package com.practice.chat.chat.consumer;

import com.practice.chat.chat.domain.ChatMessage;
import com.practice.chat.chat.service.SendMessageService;
import com.practice.chat.chat.stat.ChatTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {


    private final ObjectMapper objectMapper;
    private final SendMessageService sendMessageService;


    @KafkaListener(topics = "${kafka.topic.chat}" )
    public void consume(String message) throws JsonProcessingException {
        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
        sendMessageService.sendChatMessage(chatMessage);
    }
}
