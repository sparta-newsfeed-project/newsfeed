package com.example.newsfeed.comment.controller;

import com.example.newsfeed.comment.dto.CommentDetailResponseDto;
import com.example.newsfeed.comment.dto.CommentRequestDto;
import com.example.newsfeed.comment.dto.CommentSimpleResponseDto;
import com.example.newsfeed.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentSimpleResponseDto> saveComment(@PathVariable Long postId, @RequestBody CommentRequestDto dto) {
        return ResponseEntity.ok(commentService.saveComment(postId, dto));
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<List<CommentDetailResponseDto>> getAllComments(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getAllComments(postId));
    }
}
