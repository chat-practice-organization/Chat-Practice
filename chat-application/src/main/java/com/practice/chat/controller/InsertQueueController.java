package com.practice.chat.controller;

import domain.ChatMessage;
import com.practice.chat.producer.ChatMessageProducer;
import repository.redis.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.sql.Timestamp;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InsertQueueController {

    @Value("${kafka.topic.chat.send}")
    private String CHAT_MESSAGES_SEND_TOPIC;

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageProducer chatMessageProducer;
    private final ExecutorService executorService = Executors.newFixedThreadPool(500);
    private static final Logger logger = LoggerFactory.getLogger(InsertQueueController.class);


    // 채팅 메시지 받아서 메시지 큐와 db에 저장
    @MessageMapping("/chat/message")
    public void insertChatMessage(ChatMessage chatMessage) {
        chatMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        executorService.submit(()->chatMessageRepository.save(chatMessage));
        executorService.submit(()->chatMessageProducer.produceChatMessage(CHAT_MESSAGES_SEND_TOPIC, chatMessage));

    }

    @MessageMapping("/chat/cache")
    public void handleChat(Map<String, String> chatCacheAction) {

        String roomId = chatCacheAction.get("roomId");
        String action = chatCacheAction.get("action");
        chatMessageProducer.produceCacheSyncMessage("sync-cache-topic", roomId, action);
    }


    @GetMapping(value = "/")
    public ResponseEntity<String> getHotDeals() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("hello22");

    }

}
