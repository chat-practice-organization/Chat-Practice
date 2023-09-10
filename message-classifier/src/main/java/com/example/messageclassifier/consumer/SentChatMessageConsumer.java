package com.example.messageclassifier.consumer;

import com.example.messageclassifier.producer.PreprocessedChatMessageProducer;
import com.example.messageclassifier.service.ClassifyMessageService;
import com.example.messageclassifier.service.CacheService;
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
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
@Slf4j
public class SentChatMessageConsumer {


    private final ObjectMapper objectMapper;
    private final ClassifyMessageService classifyMessageService;
    private final CacheService cacheService;


    @KafkaListener(topics = "${kafka.topic.chat.send}", containerFactory = "kafkaBatchListenerContainerFactory")
    public void consume(List<String> messages) {

//        long startTime = System.nanoTime();
        log.info("batch size in message classifier:" + messages.size());
        messages.forEach(message -> {
            ChatMessage chatMessage = null;
            try {
                chatMessage = objectMapper.readValue(message, ChatMessage.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }

            if (chatMessage != null) classifyMessageService.classify(chatMessage);

        });

//        long endTime = System.nanoTime();
//        double duration = (double) (endTime - startTime) / 1_000_000;  // nano -> milli
//        log.info("Batch finished: {}",messages.size());
//        log.info("Batch process execution time: {}",duration);
//        log.info("Average execution time per a message: {}",duration/messages.size());
//        log.info("Message processed per a second: {}",1000/(duration/messages.size()));
    }


    @KafkaListener(topics = "sync-cache-topic", containerFactory = "uniqueContainerFactory")
    public void cacheSyncConsume(String message) {
        try {
            Map<String, String> receivedMessage = new ObjectMapper().readValue(message, Map.class);
            log.info("Cache Sync Message Received");
            cacheService.evictAllPartitionWasConnectionCache();
            cacheService.evictAllRoutingTableCache();
            cacheService.evictAllChatRoomMemberByChatRoomIdCache();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
