package com.example.newsfeed.comment.dto;

import com.example.newsfeed.comment.domain.Comment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Getter
public class CommentResponseDto {
    private Long id;
    private String content;
    private Long postId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static CommentResponseDto from(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .postId((comment.getPost().getId()))
                .userName(comment.getUser().getName())
                .userName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .deletedAt(comment.getDeletedAt())
                .build();
    }
}
