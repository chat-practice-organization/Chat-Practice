package com.practice.auth.security;

import domain.Account;
import io.jsonwebtoken.Claims;
import org.springframework.security.core.context.SecurityContextHolder;

public class CustomSecurityContextHolder extends SecurityContextHolder {
    public static Claims getClaims() {
        return (Claims) getContext().getAuthentication().getPrincipal();
    }
}
