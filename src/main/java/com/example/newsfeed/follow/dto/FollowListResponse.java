package com.example.newsfeed.follow.dto;

import com.example.newsfeed.user.domain.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FollowListResponse {

    private Long userId;

    private String userName;

    public static FollowListResponse from(User user) {
        return FollowListResponse.builder()
                .userId(user.getId())
                .userName(user.getName())
                .build();
    }
}
