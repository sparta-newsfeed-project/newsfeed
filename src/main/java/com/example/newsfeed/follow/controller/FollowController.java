package com.example.newsfeed.follow.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.follow.dto.FollowListResponse;
import com.example.newsfeed.follow.service.FollowService;
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

    @PostMapping("/{userId}")
    public ResponseEntity<Void> follow(
            @Authenticated Long currentUserId,
            @PathVariable Long userId
    ) {
        followService.createFollow(currentUserId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<?> unfollow(
            @Authenticated Long currentUserId,
            @PathVariable Long userId
    ) {
        followService.deleteFollow(currentUserId, userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/following/{userId}")
    public ResponseEntity<Page<FollowListResponse>> followingList(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(
                followService.getFollowingList(userId, pageable), HttpStatus.OK
        );
    }

    @GetMapping("/followers/{userId}")
    public ResponseEntity<Page<FollowListResponse>> followerList(
            @PathVariable Long userId,
            Pageable pageable
    ) {
        return new ResponseEntity<>(
                followService.getFollowersList(userId, pageable), HttpStatus.OK
        );
    }
}
