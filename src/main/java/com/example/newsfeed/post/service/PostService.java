package com.example.newsfeed.post.service;

import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.global.pagination.PaginationResponse;
import com.example.newsfeed.post.domain.Post;
import com.example.newsfeed.post.dto.PostRequest;
import com.example.newsfeed.post.dto.PostResponse;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public PostResponse create(Long userId, PostRequest request) {
        User user = getUserById(userId);
        Post post = postRepository.save(
                Post.builder()
                        .user(user)
                        .title(request.getTitle())
                        .content(request.getContent())
                        .build()
        );

        postRepository.save(post);
        return PostResponse.from(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long postId) {
        return PostResponse.from(getPostById(postId));
    }

    @Transactional(readOnly = true)
    public PaginationResponse<PostResponse> getPosts(Long userId, Pageable pageable) {
        if(userId == null) {
            return new PaginationResponse<>(getAll(pageable).map(PostResponse::from));
        }

        User user = getUserById(userId);
        return new PaginationResponse<>(getAllByUser(user,pageable).map(PostResponse::from));
    }

    @Transactional
    public PostResponse update(Long userId, Long postId, PostRequest request) {
        Post post = getPostById(postId);
        if(!post.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionType.NO_PERMISSION_ACTION);
        }

        post.update(request.getTitle(), request.getContent());
        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long userId, Long postId) {
        Post post = getPostById(postId);
        if(!post.getUser().getId().equals(userId)) {
            throw new CustomException(ExceptionType.NO_PERMISSION_ACTION);
        }

        postRepository.delete(post);
        // TODO : Comment 삭제 필요
    }

    public Post getPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(()-> new CustomException(ExceptionType.POST_NOT_FOUND));
    }

    private Page<Post> getAllByUser(User user, Pageable pageable) {
        return postRepository.findAllByUser(user, pageable);
    }

    private Page<Post> getAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public PaginationResponse<PostResponse> getFollowingPosts(Long userId, Pageable pageable) {
        User user = getUserById(userId);
        Page<Post> posts = postRepository.findAllByFollowing(user, pageable);

        return new PaginationResponse<>(posts.map(PostResponse::from));
    }

    private User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }
}
