package com.example.newsfeed.comment.service;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.comment.dto.CommentCreateRequestDto;
import com.example.newsfeed.comment.dto.CommentResponseDto;
import com.example.newsfeed.comment.dto.CommentSimpleResponseDto;
import com.example.newsfeed.comment.pagination.Paging;
import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.post.domain.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentSimpleResponseDto saveComment(Long postId, CommentCreateRequestDto dto) {
        User user = userRepository.findById(dto.getUserId()).orElseThrow(
                () -> new IllegalStateException("User not found")
        );

        Post post = postRepository.findById(postId).orElseThrow(
                () -> new IllegalStateException("Post not found")
        );

        Comment comment = new Comment(user, post, dto.getContent());
        Comment savedComment = commentRepository.save(comment);

        return new CommentSimpleResponseDto(
                savedComment.getId(),
                savedComment.getUser().getId(),
                savedComment.getPost().getId(),
                savedComment.getContent()
        );
    }

    public Paging.Response getAllComments(Long postId, LocalDate updatedAt, Paging.Request pagingRequest) {
        List<CommentResponseDto> pagingData =  commentRepository.findAllByPostIdAndUpdatedAt(postId, updatedAt, pagingRequest)
                .stream()
                .map(CommentResponseDto::from)
                .toList();

        return Paging.Response.builder()
                .data(pagingData.isEmpty() ? new Object[0] : pagingData.toArray())
                .size(pagingRequest.getSize())
                .page(pagingRequest.getPage())
                .build();
    }
}
