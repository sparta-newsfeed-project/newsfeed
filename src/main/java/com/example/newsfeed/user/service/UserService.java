package com.example.newsfeed.user.service;

import com.example.newsfeed.auth.config.PasswordEncoder;
import com.example.newsfeed.comment.repository.CommentRepository;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.follow.repository.FollowRepository;
import com.example.newsfeed.post.repository.PostRepository;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.dto.UserRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.RegisterRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.WithdrawRequestDto;
import com.example.newsfeed.user.dto.UserResponseDto;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void register(RegisterRequestDto requestDto) {
        validateNotDuplicatedEmail(requestDto.getEmail());

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        User user = User.builder()
                .email(requestDto.getEmail())
                .name(requestDto.getName())
                .password(encodedPassword)
                .build();

        userRepository.save(user);
    }

    @Transactional
    public void withdraw(Long userId, WithdrawRequestDto requestDto) {
        User user = userRepository.findByIdOrThrowNotFound(userId);

        validatePasswordMatch(requestDto.getPassword(), user.getPassword());

        userRepository.delete(user);
        postRepository.softDeleteByUserId(userId);
        commentRepository.softDeleteByUserId(userId);
        commentRepository.softDeleteByPostOwnerId(userId);
        followRepository.softDeleteFollowsByUserId(userId);
    }

    @Transactional(readOnly = true)
    public UserResponseDto getUserProfile(Long userId) {
        User user = getUserById(userId);
        return UserResponseDto.from(user)
                .setFollowerCount(followRepository.countByFollowed(user))
                .setFollowingCount(followRepository.countByFollower(user));
    }

    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));
    }

    /**
     * 전체 유저 목록 조회
     */
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    /**
     * 특정 사용자의 프로필을 수정
     */
    @Transactional
    public void updateProfile(Long userId, String name, String introText) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        if (name == null || name.trim().isEmpty()) {
            throw new CustomException(ExceptionType.INVALID_REQUEST);
        }

        if (introText != null && introText.length() > 200) {
            throw new CustomException(ExceptionType.INVALID_REQUEST);
        }

        user.update(name, introText);
    }

    /**
     * 특정 사용자의 비밀번호 변경
     */
    @Transactional
    public void changePassword(Long userId, UserRequestDto.ChangePasswordRequestDto requestDto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(requestDto.getCurrentPassword(), user.getPassword())) {
            throw new CustomException(ExceptionType.INVALID_CREDENTIALS, "현재 비밀번호가 일치하지 않습니다.");
        }

        if (requestDto.getCurrentPassword().equals(requestDto.getNewPassword())) {
            throw new CustomException(ExceptionType.INVALID_REQUEST, "현재 비밀번호로 변경할 수 없습니다.");
        }

        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));

    }


    private void validateNotDuplicatedEmail(String email) {
        userRepository.findByEmailWithDeleted(email)
                .ifPresent(user -> {
                    ExceptionType exceptionType = (user.getDeletedAt() == null)
                            ? ExceptionType.DUPLICATE_EMAIL
                            : ExceptionType.DELETED_ACCOUNT_EMAIL;
                    throw new CustomException(exceptionType);
                });
    }

    private void validatePasswordMatch(String inputPassword, String encodedPassword) {
        if (!passwordEncoder.matches(inputPassword, encodedPassword)) {
            throw new CustomException(ExceptionType.INVALID_PASSWORD);
        }
    }
}
