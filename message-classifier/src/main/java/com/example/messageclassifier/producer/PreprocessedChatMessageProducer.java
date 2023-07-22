package com.example.messageclassifier.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ChatMessage;
import dto.PreprocessedChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PreprocessedChatMessageProducer {
    private final KafkaTemplate kafkaTemplate;
    private final ObjectMapper objectMapper;

    
    public void producePreprocessedChatMessage(String topic, PreprocessedChatMessage preprocessedChatMessage, Integer wasId) {
        try {
            kafkaTemplate.send(topic, wasId.toString(),objectMapper.writeValueAsString(preprocessedChatMessage));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

}
