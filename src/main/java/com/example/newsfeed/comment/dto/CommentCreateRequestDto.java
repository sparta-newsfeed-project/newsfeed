package com.example.newsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentCreateRequestDto {
    private Long userId;
    private String content;
}
