package com.example.newsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    private Long userId;
    private String content;
}
