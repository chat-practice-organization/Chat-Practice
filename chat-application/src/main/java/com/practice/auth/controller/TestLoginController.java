package com.practice.auth.controller;

import com.practice.auth.dto.TokenRequest;
import com.practice.auth.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TestLoginController {

    private final TokenProvider tokenProvider;

    @GetMapping("/token/test")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<String> testLogin() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(tokenProvider.generateToken(TokenRequest.builder().accountId(1l).accountRole("USER").build()));
    }
}
