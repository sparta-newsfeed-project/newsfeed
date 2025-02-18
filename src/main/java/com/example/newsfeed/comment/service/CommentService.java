package com.example.newsfeed.comment.service;

import com.example.newsfeed.comment.domain.Comment;
import com.example.newsfeed.comment.dto.CommentCreateRequestDto;
import com.example.newsfeed.comment.dto.CommentResponseDto;
import com.example.newsfeed.comment.dto.CommentSimpleResponseDto;
import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.global.pagination.PaginationResponse;
import com.example.newsfeed.post.domain.Post;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    // TODO : 1. updatedAt 내림차순으로 정렬되도록 만들어주세요
    // TODO : 2. Pagination 공통 객체가 생성되면 해당 객체를 반환하도록 수정해주세요
    @Transactional(readOnly = true)
    public PaginationResponse<CommentResponseDto> getAllComments(Long postId, Pageable pageable) {
        Page<Comment> comments =  commentRepository.findAllByPostId(postId, pageable);

        return new PaginationResponse<>(comments.map(CommentResponseDto::from));
    }
}
