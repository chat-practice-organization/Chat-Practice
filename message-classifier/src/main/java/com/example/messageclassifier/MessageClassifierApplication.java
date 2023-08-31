package com.example.messageclassifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import co.elastic.apm.attach.ElasticApmAttacher;
import org.springframework.scheduling.annotation.EnableScheduling;


import java.util.Timer;
import java.util.TimerTask;

import org.springframework.cache.annotation.EnableCaching;


@SpringBootApplication
@EnableCaching
public class MessageClassifierApplication {
    private static final Logger logger = LoggerFactory.getLogger(MessageClassifierApplication.class);

    public static void main(String[] args) {
        ElasticApmAttacher.attach();
        SpringApplication.run(MessageClassifierApplication.class, args);
        startMemoryLogging();

    }

    private static void startMemoryLogging() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Runtime runtime = Runtime.getRuntime();
                long usedMemory = (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024;
                long maxMemory = runtime.maxMemory() / 1024 / 1024;

                logger.info("Used memory: {} MB, Max memory: {} MB", usedMemory, maxMemory);
            }
        }, 0, 3000);
    }
}