package com.example.newsfeed.comment.controller;

import com.example.newsfeed.comment.dto.CommentCreateRequestDto;
import com.example.newsfeed.comment.dto.CommentSimpleResponseDto;
import com.example.newsfeed.comment.pagination.Paging;
import com.example.newsfeed.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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
}
