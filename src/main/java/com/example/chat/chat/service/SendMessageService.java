package com.example.chat.chat.service;

import com.example.chat.chat.domain.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void sendMessage(String topic, ChatMessage chatMessage) {
        try {
            kafkaTemplate.send(topic, objectMapper.writeValueAsString(chatMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
