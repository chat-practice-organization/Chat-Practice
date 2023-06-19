package com.practice.chat.chat.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SendMessageServiceTest {

    @Autowired
    private InsertQueueService insertQueueService;


    @Test
    void sendMessage() {
    }
}