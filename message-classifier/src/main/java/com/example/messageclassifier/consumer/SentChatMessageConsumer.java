package com.example.messageclassifier.consumer;

import com.example.messageclassifier.producer.PreprocessedChatMessageProducer;
import com.example.messageclassifier.service.ClassifyMessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import domain.ChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;
import repository.jpa.ChatRoomMemberRepository;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class SentChatMessageConsumer {


    private final ObjectMapper objectMapper;
    private final ClassifyMessageService classifyMessageService;


    @KafkaListener(topics = "${kafka.topic.chat.send}", containerFactory = "kafkaBatchListenerContainerFactory")
    public void consume(List<String> messages) {
        log.info("batch size:" + messages.size());
        messages.forEach(message -> {
            ChatMessage chatMessage = null;
            try {
                chatMessage = objectMapper.readValue(message, ChatMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if (chatMessage != null) classifyMessageService.classify(chatMessage);

        });
    }
}
