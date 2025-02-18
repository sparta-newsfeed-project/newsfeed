package com.example.newsfeed.follow.service.repository;

import com.example.newsfeed.follow.domain.Follow;
import com.example.newsfeed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<Follow, Long> {

    Boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<Follow> findByFollowerAndFollowed(User follower, User followed);

    @Query("SELECT f FROM Follow f JOIN FETCH f.followed WHERE f.follower = :user")
    Page<Follow> findAllByFollower(@Param("user") User user, Pageable pageable);

    @Query("SELECT f FROM Follow f JOIN FETCH f.follower WHERE f.followed = :user")
    Page<Follow> findAllByFollowed(@Param("user")User user, Pageable pageable);
}
