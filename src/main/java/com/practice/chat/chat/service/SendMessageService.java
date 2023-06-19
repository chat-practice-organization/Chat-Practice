package com.practice.chat.chat.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.practice.chat.chat.domain.ChatMessage;
import com.practice.chat.repository.jpa.ChatRoomMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SendMessageService {
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    // 해당 채팅방 구성원 조회 후 각각 소켓에 뿌려줌
    public void sendChatMessage(ChatMessage chatMessage) {
        chatRoomMemberRepository.findChatRoomMemberJpaByChatRoomId(chatMessage.getChatRoomId())
                .forEach(chatRoomMember -> {
                    simpMessagingTemplate.convertAndSend("/sub/user/"+chatRoomMember.getSessionId(),chatMessage);
                });
    }
}
