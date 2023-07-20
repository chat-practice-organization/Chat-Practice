package com.practice.chat.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.chat.chat.domain.ChatMessage;
import com.practice.chat.chat.domain.ChatRoomMember;
import com.practice.chat.chat.dto.PreProcessedChatMessage;
import com.practice.chat.repository.jpa.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendChatMessage(PreProcessedChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSend("/sub/user/" + chatMessage.getReceiverSessionId(), chatMessage);
    }

}
