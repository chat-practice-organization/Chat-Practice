package com.practice.chat.producer;

import domain.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatMessageProducer {
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;


    // 채팅 메시지 메시지 큐에 삽입
    public void produceChatMessage(String topic, ChatMessage chatMessage) {
        try {
            kafkaTemplate.send(topic, chatMessage.getChatRoomId().toString(),objectMapper.writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }


    public void produceCacheSyncMessage(String topic, String roomId, String action) {
        Map<String, String> message = new HashMap<>();
        message.put("roomId", roomId);
        message.put("action", action);
        
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
    
}
