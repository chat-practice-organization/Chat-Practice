package com.practice.chat.controller;

import domain.ChatMessage;
import com.practice.chat.producer.ChatMessageProducer;
import com.practice.chat.repository.redis.chat.ChatMessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${kafka.topic.chat.send}")
    private String CHAT_MESSAGES_SEND_TOPIC;

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageProducer chatMessageProducer;


    // 채팅 메시지 받아서 메시지 큐와 db에 저장
    @MessageMapping("/chat/message")
    public void insertChatMessage(ChatMessage chatMessage) {
        chatMessage.setCreatedAt(new Timestamp(System.currentTimeMillis()));
        chatMessageRepository.save(chatMessage);
        chatMessageProducer.produceChatMessage(CHAT_MESSAGES_SEND_TOPIC, chatMessage);
    }



    @GetMapping(value = "/")
    public ResponseEntity<String> getHotDeals() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body("hello22");

    }

}
