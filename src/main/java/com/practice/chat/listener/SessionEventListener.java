package com.practice.chat.listener;

import com.practice.chat.service.UpdateRoutingTableService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;


@Component
@Slf4j
@RequiredArgsConstructor
public class SessionEventListener {

    private final UpdateRoutingTableService updateRoutingTableService;

    @EventListener
    public void sessionConnectEventHandler(SessionConnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        updateRoutingTableService.insert(sessionId);
    }

    @EventListener
    public void sessionDisconnectEventHandler(SessionDisconnectEvent event) {
        String sessionId = StompHeaderAccessor.wrap(event.getMessage()).getSessionId();
        updateRoutingTableService.deleteIfExist(sessionId);
    }

}
