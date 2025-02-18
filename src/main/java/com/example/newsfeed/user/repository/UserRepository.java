package com.example.newsfeed.user.repository;

import com.example.newsfeed.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
