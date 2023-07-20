package com.practice.chat.chat.service;

import com.practice.chat.chat.domain.ChatMessage;
import com.practice.chat.chat.domain.RoutingTable;
import com.practice.chat.repository.jpa.ChatRoomMemberRepository;
import com.practice.chat.repository.redis.chat.RoutingTableRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateRoutingTableService {

    private static final Integer WAS_ID = getWasId();
    private final RoutingTableRepository routingTableRepository;

    private static Integer getWasId() {
        String podName = System.getenv("HOSTNAME");
        if (podName == null) {
            log.info("Here is local machine");
            return 1;
        }

        Matcher matcher = Pattern.compile("[0-9]+").matcher(podName);
        while (matcher.find()) {
            Integer wasId = Integer.valueOf(matcher.group());
            log.info("WAS_ID is "+wasId);
            return wasId;
        }

        log.info("WAS_ID cannot found.");
        return 1;
    }

    public void insert(String sessionId) {
        routingTableRepository.save(
                RoutingTable.builder()
                        .sessionId(sessionId)
                        .wasId(WAS_ID)
                        .build()
        );
    }

    public void deleteIfExist(String sessionId) {
        routingTableRepository.findById(sessionId).ifPresent(routingTableRepository::delete);
    }


}
