package com.example.newsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentSimpleResponseDto {
    private final Long id;
    private final Long userId;
    private final Long postId;
    private final String content;

    public CommentSimpleResponseDto(Long id, Long userId, Long postId, String content) {
        this.id = id;
        this.userId = userId;
        this.postId = postId;
        this.content = content;
    }
}
