package com.example.chat.chat.domain;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
public class ChatMessage {

    private Long chatRoomId;
    private String content;
}
