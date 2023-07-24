package com.practice.chat.config;

import com.practice.chat.service.SaveChatRoomMemberService;
import domain.ChatRoomMember;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import repository.jpa.ChatRoomMemberRepository;

@RequiredArgsConstructor
@Component
public class CustomChannelInterceptor implements ChannelInterceptor {

    private final SaveChatRoomMemberService saveChatRoomMemberService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.SUBSCRIBE.equals(accessor.getCommand())) {
            accessor.setDestination("/sub/user/"+accessor.getSessionId());
            saveChatRoomMemberService.save(ChatRoomMember.builder().chatRoomId(1l).sessionId(accessor.getSessionId()).build());
            return MessageBuilder.createMessage(message.getPayload(), accessor.getMessageHeaders());
        }
        return message;
    }
}
