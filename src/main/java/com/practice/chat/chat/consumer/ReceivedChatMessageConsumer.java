package com.practice.chat.chat.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.chat.chat.dto.PreProcessedChatMessage;
import com.practice.chat.chat.service.SendMessageService;
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


    @KafkaListener(containerFactory = "kafkaBatchListenerContainerFactory", topicPartitions = @TopicPartition(topic = "${kafka.topic.chat.receive}", partitions = "${was.id}"))
    public void consume(List<String> messages) {
        log.info("batch size:" + messages.size());
        messages.forEach(message -> {
            PreProcessedChatMessage chatMessage = null;
            try {
                chatMessage = objectMapper.readValue(message, PreProcessedChatMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
            sendMessageService.sendChatMessage(chatMessage);

        });
        count.addAndGet(messages.size());
    }
}
