package com.example.newsfeed.comment.service;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.comment.dto.CommentDetailResponseDto;
import com.example.newsfeed.comment.dto.CommentRequestDto;
import com.example.newsfeed.comment.dto.CommentSimpleResponseDto;
import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.post.domain.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentSimpleResponseDto saveComment(Long postId, CommentRequestDto dto) {
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

    public List<CommentDetailResponseDto> getAllComments(Long postId) {
        return commentRepository.findAll().stream()
                .map(comment -> new CommentDetailResponseDto(
                        comment.getId(),
                        comment.getPost().getId(),
                        comment.getContent(),
                        userRepository.findByComment(comment).getName()
                )).collect(Collectors.toList());

    }
}
