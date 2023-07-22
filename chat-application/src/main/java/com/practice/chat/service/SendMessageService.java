package com.practice.chat.service;

import dto.PreprocessedChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SendMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    public void sendChatMessage(PreprocessedChatMessage chatMessage) {
        simpMessagingTemplate.convertAndSend("/sub/user/" + chatMessage.getReceiverSessionId(), chatMessage);
    }

}
