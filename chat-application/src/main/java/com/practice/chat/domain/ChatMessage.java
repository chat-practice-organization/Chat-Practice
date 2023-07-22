package com.practice.chat.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@RedisHash(value = "chatMessage")
public class ChatMessage {

    @Id
    private String chatMessageId;
    private Long chatRoomId;
    private String content;
    private Timestamp createdAt;

}
