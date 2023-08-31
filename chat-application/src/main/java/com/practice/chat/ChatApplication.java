package com.practice.chat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import co.elastic.apm.attach.ElasticApmAttacher;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChatApplication {

    private static final Logger logger = LoggerFactory.getLogger(ChatApplication.class);

    public static void main(String[] args) {
        // ElasticApmAttacher.attach();
        SpringApplication.run(ChatApplication.class, args);
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
        }, 0, 500);
    }
}
