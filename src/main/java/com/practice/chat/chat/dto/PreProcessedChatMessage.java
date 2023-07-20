package com.practice.chat.chat.dto;

import com.practice.chat.chat.domain.ChatMessage;
import lombok.*;

import java.sql.Timestamp;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class PreProcessedChatMessage {
    private ChatMessage chatMessage;
    private String receiverSessionId;
}
