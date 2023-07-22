package com.practice.chat.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@RedisHash(value = "routingTable")
public class RoutingTable {

    @Id
    private String sessionId;
    private Integer wasId;

}
