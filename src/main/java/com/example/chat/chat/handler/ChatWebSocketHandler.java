package com.example.chat.chat.handler;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;

@RequiredArgsConstructor
@Component
@Slf4j
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final RabbitTemplate rabbitTemplate;

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        Map<String, Object> attributes = session.getAttributes();
        String chatRoomId = session.getUri().getPath().substring(session.getUri().getPath().lastIndexOf('/') + 1);
        log.info("roomId:"+chatRoomId);
        rabbitTemplate.convertAndSend("amq.direct","chatRoom."+chatRoomId,message.getPayload());
    }


}
