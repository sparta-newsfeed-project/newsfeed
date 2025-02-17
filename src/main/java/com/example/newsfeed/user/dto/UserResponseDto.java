package com.example.newsfeed.user.dto;

import com.example.newsfeed.user.domain.User;
import lombok.Getter;

@Getter
public class UserResponseDto {
    // API 응답용 DTO
    private Long id;
    private String name;
    private String email;
    private String introText;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.introText = user.getIntroText();
    }

}
