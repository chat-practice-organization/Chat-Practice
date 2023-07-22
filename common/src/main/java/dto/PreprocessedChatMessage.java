package dto;

import domain.ChatMessage;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
public class PreprocessedChatMessage {
    private ChatMessage chatMessage;
    private String receiverSessionId;

    public static PreprocessedChatMessage from(ChatMessage chatMessage, String receiverSessionId) {
        return new PreprocessedChatMessage(chatMessage, receiverSessionId);
    }
}
