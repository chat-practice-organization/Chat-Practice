package com.practice.chat.service;

import domain.ChatRoomMember;
import dto.PreprocessedChatMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import repository.jpa.ChatRoomMemberRepository;

@Service
@RequiredArgsConstructor
public class SaveChatRoomMemberService {

    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional
    public void save(ChatRoomMember chatRoomMember) {
        chatRoomMemberRepository.save(chatRoomMember);
    }

}
