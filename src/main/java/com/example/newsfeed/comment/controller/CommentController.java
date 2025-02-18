package com.example.newsfeed.comment.controller;

import com.example.newsfeed.comment.dto.CommentCreateRequestDto;
import com.example.newsfeed.comment.dto.CommentResponseDto;
import com.example.newsfeed.comment.dto.CommentSimpleResponseDto;
import com.example.newsfeed.comment.service.CommentService;
import com.example.newsfeed.global.pagination.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentSimpleResponseDto> saveComment(@PathVariable Long postId, @RequestBody CommentCreateRequestDto dto) {
        return ResponseEntity.ok(commentService.saveComment(postId, dto));
    }

    @GetMapping("/api/posts/{postId}/comments")
    public ResponseEntity<PaginationResponse<CommentResponseDto>> getAllComments(
            @PathVariable Long postId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(
                commentService.getAllComments(postId, pageable), HttpStatus.OK
        );
    }

}
