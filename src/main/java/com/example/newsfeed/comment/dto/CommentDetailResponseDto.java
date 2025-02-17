package com.example.newsfeed.comment.dto;

import lombok.Getter;

@Getter
public class CommentDetailResponseDto {
    private final Long id;
    private final Long postId;
    private final String content;
    private final String userName;

    public CommentDetailResponseDto(Long id, Long postId, String content, String userName) {
        this.id = id;
        this.postId = postId;
        this.content = content;
        this.userName = userName;
    }
}
