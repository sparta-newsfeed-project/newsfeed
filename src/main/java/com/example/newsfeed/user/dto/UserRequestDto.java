package com.example.newsfeed.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserRequestDto {
    // 클라이언트가 보낸 요청 데이터를 담는 DTO
    private String name;
    private String email;
    private String password;
    private String introText;
}
