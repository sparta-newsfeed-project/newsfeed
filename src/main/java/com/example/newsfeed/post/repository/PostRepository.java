package com.example.newsfeed.post.repository;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.post.domain.Post;
import jakarta.servlet.http.HttpSession;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
    Post findByComment(Comment comment);
}
