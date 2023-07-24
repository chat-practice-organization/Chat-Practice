package com.practice.chat.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dto.PreprocessedChatMessage;
import com.practice.chat.service.SendMessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.TopicPartition;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceivedChatMessageConsumer {


    public static final AtomicInteger count = new AtomicInteger();
    private final ObjectMapper objectMapper;
    private final SendMessageService sendMessageService;


//    @KafkaListener(containerFactory = "kafkaBatchListenerContainerFactory", topicPartitions = @TopicPartition(topic = "${kafka.topic.chat.receive}", partitions = "${was.id}"))
//    public void consume(List<String> messages) {
//        log.info("batch size:" + messages.size());
//        messages.forEach(message -> {
//            PreprocessedChatMessage preprocessedChatMessage = null;
//            try {
//                preprocessedChatMessage = objectMapper.readValue(message, PreprocessedChatMessage.class);
//            } catch (JsonProcessingException e) {
//                e.printStackTrace();
//            }
//            if (preprocessedChatMessage != null) sendMessageService.sendChatMessage(preprocessedChatMessage);
//
//        });
//
//        count.addAndGet(messages.size());
//    }

    @KafkaListener(topics = "${kafka.topic.chat.receive}",containerFactory = "kafkaBatchListenerContainerFactory")
    public void consume(String message) {


        PreprocessedChatMessage preprocessedChatMessage = null;
        try {
            preprocessedChatMessage = objectMapper.readValue(message, PreprocessedChatMessage.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        if (preprocessedChatMessage != null) sendMessageService.sendChatMessage(preprocessedChatMessage);


    }
}
