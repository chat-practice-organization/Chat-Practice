package com.practice.chat.repository.redis.chat;


import com.practice.chat.domain.ChatMessage;
import org.springframework.data.repository.CrudRepository;

public interface ChatMessageRepository extends CrudRepository<ChatMessage,String> {
}
