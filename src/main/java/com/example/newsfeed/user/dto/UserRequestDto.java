package com.example.newsfeed.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class UserRequestDto {
    @Getter
    @AllArgsConstructor
    public static class RegisterRequestDto {
        @NotBlank
        @Size(max = 255)
        @Pattern(
                regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
                message = "이메일 형식이어야 합니다"
        )
        private final String email;

        @NotBlank
        @Size(min = 8)
        @Pattern(
                regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "비밀번호는 영문(대소문자) + 숫자 + 특수문자를 각각 1개 이상 포함해야 합니다."
        )
        private final String password;

        @NotBlank
        @Size(max = 100)
        private final String name;
    }

    @Getter
    @AllArgsConstructor
    public static class WithdrawRequestDto {
        @NotBlank
        @Size(min = 8)
        private final String password;
    }
}
