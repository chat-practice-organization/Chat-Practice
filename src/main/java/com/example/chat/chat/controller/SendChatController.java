package com.example.chat.chat.controller;

import com.example.chat.chat.domain.ChatMessage;
import com.example.chat.chat.service.SendMessageService;
import com.example.chat.chat.stat.ChatTopics;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class SendChatController {

    private final SendMessageService sendMessageService;

    @MessageMapping("/chat/message")
    public void sendChatMessage(ChatMessage chatMessage) {
        log.info(chatMessage.toString());
        sendMessageService.sendMessage(ChatTopics.CHAT_MESSAGES_TOPIC,chatMessage);
    }

}
