package com.practice.chat.consumer;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@EnableScheduling
@Component
@Slf4j
public class PrintCounterScheduler {
    @Scheduled(cron = "0/5 * * * * ?")
    public void printConsumerCount() {
        log.info("consuming: "+ ReceivedChatMessageConsumer.count);
    }
}
