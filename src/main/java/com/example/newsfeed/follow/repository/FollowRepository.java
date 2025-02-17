package com.example.newsfeed.follow.repository;

import com.example.newsfeed.follow.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FollowRepository extends JpaRepository<Follow, Long> {
}
