package com.example.newsfeed.comment.repository;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.comment.dto.CommentDetailResponseDto;
import com.example.newsfeed.comment.pagination.Paging;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<CommentDetailResponseDto> findAllByPostIdAndUpdatedAt(Long postId, LocalDate updatedAt, Paging.Request pagingRequest);
}
