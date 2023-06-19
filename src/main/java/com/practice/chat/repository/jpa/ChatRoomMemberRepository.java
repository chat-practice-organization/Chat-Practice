package com.practice.chat.repository.jpa;

import com.practice.chat.chat.domain.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
    List<ChatRoomMember> findChatRoomMemberJpaByChatRoomId(Long chatRoomId);
}
