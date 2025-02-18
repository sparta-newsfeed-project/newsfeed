package com.example.newsfeed.comment.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.comment.dto.*;
import com.example.newsfeed.comment.pagination.Paging;
import com.example.newsfeed.comment.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @PostMapping("/api/posts/{postId}/comments")
    public ResponseEntity<CommentSimpleResponseDto> saveComment(@PathVariable Long postId, @RequestBody CommentCreateRequestDto dto) {
        return ResponseEntity.ok(commentService.saveComment(postId, dto));
    }

    @GetMapping
    public ResponseEntity<Paging.Response> getAllComments(
            @RequestParam(required = false) Long postId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate updatedAt,
            @RequestParam(required = false, defaultValue = "10") int size,
            @RequestParam(required = false, defaultValue = "0") int page
    ) {
        return new ResponseEntity<>(commentService.getAllComments(postId, updatedAt, new Paging.Request(size, page)), HttpStatus.OK);
    }

    @PutMapping("/api/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentUpdateResponseDto> updateComment(
            @Authenticated Long userId,
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @Valid @RequestBody CommentUpdateRequestDto dto
    ) {
        commentService.updateComment(userId, postId, commentId, dto);

        return new ResponseEntity<>(HttpStatus.OK);
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
