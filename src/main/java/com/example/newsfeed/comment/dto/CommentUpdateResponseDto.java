package com.example.newsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentUpdateResponseDto {
    private final Long id;
    private final String content;

    public CommentUpdateResponseDto(Long id, String content) {
        this.id = id;
        this.content = content;
    }
}
