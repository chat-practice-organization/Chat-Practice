package com.example.messageclassifier.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.example.messageclassifier.MessageClassifierApplication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CacheService {
    private static final Logger log = LoggerFactory.getLogger(CacheService.class);

    @CacheEvict(value = "partitionWasConnection", allEntries = true)
    public void evictAllPartitionWasConnectionCache() {
        log.info("partitionWasConnection Cache Clear");
    }

    @CacheEvict(value = "routingTable", allEntries = true)
    public void evictAllRoutingTableCache() {
        log.info("routingTable Cache Clear");
    }

    @CacheEvict(value = "chatRoomMemberByChatRoomId", allEntries = true)
    public void evictAllChatRoomMemberByChatRoomIdCache() {
        log.info("chatRoomMemberByChatRoomId Cache Clear");
    }
}
