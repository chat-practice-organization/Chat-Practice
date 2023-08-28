package com.practice.auth.dto;


import lombok.*;

@Getter
@AllArgsConstructor
@ToString
@Builder
@NoArgsConstructor
public class TokenRequest {
    private Long accountId;
    private String accountRole;

}
