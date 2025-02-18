package com.example.newsfeed.auth.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class AuthRequestDto {

    @Getter
    @AllArgsConstructor
    public static class LoginRequestDto {
        @NotBlank
        @Size(max = 255)
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "이메일 형식이어야 합니다"
        )
        private final String email;

        @NotBlank
        @Size(min = 8)
        private final String password;
    }
}
