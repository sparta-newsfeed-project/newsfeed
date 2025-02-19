package com.example.newsfeed.post.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.global.pagination.PaginationResponse;
import com.example.newsfeed.post.dto.PostRequest;
import com.example.newsfeed.post.dto.PostResponse;
import com.example.newsfeed.post.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Authenticated Long currentUserId,
            @Valid @RequestBody PostRequest request
    ) {
        return new ResponseEntity<>(
                postService.create(currentUserId, request), HttpStatus.CREATED
        );
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> getPost(
            @PathVariable Long postId
    ) {
        return new ResponseEntity<>(postService.getPost(postId), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<PaginationResponse<PostResponse>> getPosts(
            @RequestParam(required = false) Long userId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(postService.getPosts(userId, pageable), HttpStatus.OK);
    }

    @PutMapping("/{postId}")
    public ResponseEntity<PostResponse> updatePost(
            @Authenticated Long currentUserId,
            @PathVariable Long postId,
            @Valid @RequestBody PostRequest request
    ) {
        return new ResponseEntity<>(
                postService.update(currentUserId, postId, request), HttpStatus.OK
        );
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(
            @Authenticated Long currentUserId,
            @PathVariable Long postId
    ) {
        postService.delete(currentUserId, postId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/following")
    public PaginationResponse<PostResponse> getFollowingPosts(
            @Authenticated Long currentUserId,
            Pageable pageable
    ) {
        return postService.getFollowingPosts(currentUserId, pageable);
    }
}
