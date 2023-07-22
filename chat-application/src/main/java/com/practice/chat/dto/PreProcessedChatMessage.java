package com.practice.chat.dto;

import domain.ChatMessage;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class PreProcessedChatMessage {
    private ChatMessage chatMessage;
    private String receiverSessionId;
}
