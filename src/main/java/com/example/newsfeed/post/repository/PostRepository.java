package com.example.newsfeed.post.repository;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.post.domain.Post;
import com.example.newsfeed.user.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.net.ssl.SSLSession;

public interface PostRepository extends JpaRepository<Post, Long> {

    @Query(
            value = "SELECT p FROM Post p " +
                    "JOIN FETCH p.user " +
                    "JOIN Follow f ON p.user = f.followed " +
                    "WHERE f.follower = :user " +
                    "ORDER BY p.createdAt DESC",
            countQuery = "SELECT COUNT(p) FROM Post p " +
                    "JOIN Follow f ON p.user = f.followed " +
                    "WHERE f.follower = :user"
    )
    Page<Post> findAllByFollowing(@Param("user") User user, Pageable pageable);

    @Modifying
    @Query("UPDATE Post p SET p.deletedAt = CURRENT_TIMESTAMP WHERE p.user.id = :userId AND p.deletedAt IS NULL")
    void softDeleteByUserId(@Param("userId") Long userId);

    Page<Post> findAllByUser(User user, Pageable pageable);
}
