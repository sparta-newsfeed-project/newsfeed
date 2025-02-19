package com.example.newsfeed.post.repository;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.post.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.net.ssl.SSLSession;

public interface PostRepository extends JpaRepository<Post, Long> {
}
