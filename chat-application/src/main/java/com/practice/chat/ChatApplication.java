package com.practice.chat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import co.elastic.apm.attach.ElasticApmAttacher;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
public class ChatApplication {

    public static void main(String[] args) {
        ElasticApmAttacher.attach();
        SpringApplication.run(ChatApplication.class, args);
    }
}
