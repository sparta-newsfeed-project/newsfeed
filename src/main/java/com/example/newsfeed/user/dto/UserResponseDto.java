package com.example.newsfeed.user.dto;

import com.example.newsfeed.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
public class UserResponseDto {
    // API 응답용 DTO
    private Long id;
    private String name;
    private String email;
    private String introText;
    private Long followerCount;
    private Long followingCount;

    public UserResponseDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.introText = user.getIntroText();
    }

    public static UserResponseDto from(User user) {
        return new UserResponseDto(user);
    }

    public UserResponseDto setFollowerCount(Long followerCount) {
        this.followerCount = followerCount;
        return this;
    }

    public UserResponseDto setFollowingCount(Long followingCount) {
        this.followingCount = followingCount;
        return this;
    }
}
