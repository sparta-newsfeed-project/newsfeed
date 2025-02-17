package com.example.newsfeed.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthResponseDto {
    @Getter
    @AllArgsConstructor
    public static class LoginResponseDto {
        private final Long userId;
    }
}
