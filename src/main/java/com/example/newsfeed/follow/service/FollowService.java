package com.example.newsfeed.follow.service;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.follow.domain.Follow;
import com.example.newsfeed.follow.dto.FollowListResponse;
import com.example.newsfeed.follow.repository.FollowRepository;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserService userService;

    @Transactional
    public void createFollow(Long currentUserId, Long userId) {
        checkSelfFollow(currentUserId, userId);
        checkFollowExists(currentUserId, userId);

        User followerUser = userService.getUserById(currentUserId);
        User followedUser = userService.getUserById(userId);
        Follow follow = Follow.builder()
                .follower(followerUser)
                .followed(followedUser)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void deleteFollow(Long currentUserId, Long userId) {
        User followerUser = userService.getUserById(currentUserId);
        User followedUser = userService.getUserById(userId);
        Follow follow = getFollowByFollowerAndFollowed(followerUser, followedUser);

        followRepository.delete(follow);
    }


    public Follow getFollowByFollowerAndFollowed(User followerUser, User followedUser) {
        return followRepository.findByFollowerAndFollowed(followerUser, followedUser)
                .orElseThrow(() -> new CustomException(ExceptionType.FOLLOW_NOT_FOUND));
    }

    private void checkFollowExists(Long followerId, Long followedId) {
        if (followRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new CustomException(ExceptionType.FOLLOW_ALREADY_EXISTS);
        }
    }

    private void checkSelfFollow(Long followerId, Long followedId) {
        if(followerId.equals(followedId)) {
            throw new CustomException(ExceptionType.SELF_FOLLOW_NOT_ALLOWED);
        }
    }
}
