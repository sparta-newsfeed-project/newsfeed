package com.example.newsfeed.follow.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.follow.dto.FollowListResponse;
import com.example.newsfeed.follow.service.FollowService;
import com.example.newsfeed.global.pagination.PaginationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowController {

    private final FollowService followService;

    @PostMapping("/{targetUserId}")
    public ResponseEntity<Void> follow(
            @Authenticated Long currentUserId,
            @PathVariable Long targetUserId
    ) {
        followService.createFollow(currentUserId, targetUserId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{targetUserId}")
    public ResponseEntity<?> unfollow(
            @Authenticated Long currentUserId,
            @PathVariable Long targetUserId
    ) {
        followService.deleteFollow(currentUserId, targetUserId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<PaginationResponse<FollowListResponse>> followingList(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(
                followService.getFollowingList(userId, pageable), HttpStatus.OK
        );
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<PaginationResponse<FollowListResponse>> followerList(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(
                followService.getFollowerList(userId, pageable), HttpStatus.OK
        );
    }
}
