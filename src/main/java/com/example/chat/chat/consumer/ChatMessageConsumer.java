package com.example.chat.chat.consumer;

import com.example.chat.chat.domain.ChatMessage;
import com.example.chat.chat.stat.ChatTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatMessageConsumer {



    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ObjectMapper objectMapper;


    @KafkaListener(topics = ChatTopics.CHAT_MESSAGES_TOPIC )
    public void consume(String message) throws JsonProcessingException {
        ChatMessage chatMessage = objectMapper.readValue(message, ChatMessage.class);
        simpMessagingTemplate.convertAndSend("/sub/chat/room/"+chatMessage.getChatRoomId(),chatMessage.toString());
    }
}
