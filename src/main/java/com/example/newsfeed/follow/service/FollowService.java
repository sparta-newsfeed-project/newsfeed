package com.example.newsfeed.follow.service;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.follow.domain.Follow;
import com.example.newsfeed.follow.dto.FollowListResponse;
import com.example.newsfeed.follow.repository.FollowRepository;
import com.example.newsfeed.global.pagination.PaginationResponse;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    @Transactional
    public void createFollow(Long currentUserId, Long userId) {
        checkSelfFollow(currentUserId, userId);
        checkFollowExists(currentUserId, userId);

        User followerUser = getUserById(currentUserId);
        User followedUser = getUserById(userId);
        Follow follow = Follow.builder()
                .follower(followerUser)
                .followed(followedUser)
                .build();

        followRepository.save(follow);
    }

    @Transactional
    public void deleteFollow(Long currentUserId, Long userId) {
        User followerUser = getUserById(currentUserId);
        User followedUser = getUserById(userId);
        Follow follow = getFollowByFollowerAndFollowedUser(followerUser, followedUser);

        followRepository.delete(follow);
    }

    @Transactional(readOnly = true)
    public PaginationResponse<FollowListResponse> getFollowingList(Long userId, Pageable pageable) {
        User user = getUserById(userId);
        Page<Follow> following = followRepository.findAllByFollower(user, pageable);

        return new PaginationResponse<FollowListResponse>(
                following.map(follow -> FollowListResponse.from(follow.getFollowed()))
        );
    }

    @Transactional(readOnly = true)
    public PaginationResponse<FollowListResponse> getFollowerList(Long userId, Pageable pageable) {
        User user = getUserById(userId);
        Page<Follow> followers = followRepository.findAllByFollowed(user, pageable);

        return new PaginationResponse<FollowListResponse>(
                followers.map(follow -> FollowListResponse.from(follow.getFollower()))
        );
    }

    public Follow getFollowByFollowerAndFollowedUser(User followerUser, User followedUser) {
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

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }
}
