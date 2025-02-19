package com.example.newsfeed.comment.repository;

import com.example.newsfeed.comment.domain.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Page<Comment> findAllByPostId(Long postId, Pageable pageable);
    Comment findByUserId(Long userId);
    Comment findByPostId(Long postId);

    @Modifying
    @Query("UPDATE Comment c SET c.deletedAt = CURRENT_TIMESTAMP WHERE c.user.id = :userId AND c.deletedAt IS NULL")
    void softDeleteByUserId(@Param("userId") Long userId);

    @Modifying
    @Query(value = "UPDATE comments c " +
            "JOIN posts p ON c.post_id = p.id " +
            "SET c.deleted_at = CURRENT_TIMESTAMP " +
            "WHERE p.user_id = :postOwnerId AND c.deleted_at IS NULL",
    nativeQuery = true)
    void softDeleteByPostOwnerId(@Param("postOwnerId") Long postOwnerId);

    Page<Comment> findAllByPostIdAndDeletedAtIsNull(Long postId, Pageable pageable);
}