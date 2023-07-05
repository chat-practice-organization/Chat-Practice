package com.practice.chat.chat.controller;

import com.practice.chat.chat.domain.ChatMessage;
import com.practice.chat.chat.service.InsertQueueService;
import com.practice.chat.chat.stat.ChatTopics;
import com.practice.chat.repository.redis.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;

@RestController
@Slf4j
@RequiredArgsConstructor
public class InsertQueueController {

    private final ChatMessageRepository chatMessageRepository;
    private final InsertQueueService insertQueueService;

    // 채팅 메시지 받아서 메시지 큐와 db에 저장
    @MessageMapping("/chat/message")
    public void insertChatMessage(ChatMessage chatMessage) {
        chatMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        chatMessageRepository.save(chatMessage);
        insertQueueService.insertChatMessage(ChatTopics.CHAT_MESSAGES_TOPIC, chatMessage);
    }



    @GetMapping(value = "/")
    public ResponseEntity<String> getHotDeals() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("hello");

    }

}
