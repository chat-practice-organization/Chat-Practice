package com.practice.chat.chat.service;

import com.practice.chat.chat.domain.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class InsertQueueService {
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;


    // 채팅 메시지 메시지 큐에 삽입
    public void insertChatMessage(String topic, ChatMessage chatMessage) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
