package com.practice.chat.repository.jpa;

import domain.ChatRoomMember;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember,Long> {
    @Cacheable(value = "chatRoomMemberByChatRoomId", key = "#chatRoomId.toString()",cacheManager = "redisCacheManager")
    List<ChatRoomMember> findTop500ChatRoomMemberJpaByChatRoomId(Long chatRoomId);
}
