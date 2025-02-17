package com.example.newsfeed.comment.repository;

import com.example.newsfeed.comment.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
