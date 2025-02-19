package com.example.newsfeed.comment.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.comment.dto.*;
import com.example.newsfeed.comment.service.CommentService;
import com.example.newsfeed.global.pagination.PaginationResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<CommentSimpleResponseDto> saveComment(
            @Authenticated Long userId,
            @PathVariable Long postId,
            @RequestBody CommentCreateRequestDto dto
    ) {
        return ResponseEntity.ok(commentService.saveComment(userId, postId, dto));
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

    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @Authenticated Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto
    ) {

        return new ResponseEntity<>(
                commentService.updateComment(userId, postId, commentId, dto), HttpStatus.OK);
    }

    @DeleteMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @Authenticated Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId
    ){
        commentService.deleteComment(userId,postId,commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
