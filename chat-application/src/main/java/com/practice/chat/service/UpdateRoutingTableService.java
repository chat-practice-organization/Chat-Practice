package com.practice.chat.service;

import com.practice.chat.domain.RoutingTable;
import com.practice.chat.repository.redis.chat.RoutingTableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UpdateRoutingTableService {


    @Value("${was.id}")
    private String WAS_ID;
    private final RoutingTableRepository routingTableRepository;



    public void insert(String sessionId) {
        routingTableRepository.save(
                RoutingTable.builder()
                        .sessionId(sessionId)
                        .wasId(Integer.parseInt(WAS_ID))
                        .build()
        );
    }

    public void deleteIfExist(String sessionId) {
        routingTableRepository.findById(sessionId).ifPresent(routingTableRepository::delete);
    }


}
