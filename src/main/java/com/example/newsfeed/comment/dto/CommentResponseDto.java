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

    public static CommentResponseDto from(CommentDetailResponseDto commentDetailResponseDto) {
        return CommentResponseDto.builder()
                .id(commentDetailResponseDto.getId())
                .content(commentDetailResponseDto.getContent())
                .postId((commentDetailResponseDto.getPostId()))
                .userName(commentDetailResponseDto.getUserName())
                .build();
    }
}
