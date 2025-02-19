package com.example.newsfeed.post.repository;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.post.domain.Post;
import com.example.newsfeed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.net.ssl.SSLSession;

public interface PostRepository extends JpaRepository<Post, Long> {

    @EntityGraph(attributePaths = {"user"})
    @Query(
            "SELECT p FROM Post p " +
            "WHERE p.user IN (SELECT f.followed FROM Follow f WHERE f.follower = :user) " +
            "ORDER BY p.createdAt DESC"
    )
    Page<Post> findAllByFollowing(@Param("user") User user, Pageable pageable);

    Page<Post> findAllByUser(User user, Pageable pageable);
}
