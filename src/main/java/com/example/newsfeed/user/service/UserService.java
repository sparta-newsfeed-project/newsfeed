package com.example.newsfeed.user.service;

import com.example.newsfeed.auth.config.PasswordEncoder;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.dto.UserRequestDto.RegisterRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.WithdrawRequestDto;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
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
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new CustomException(ExceptionType.INVALID_CREDENTIALS);
        }

        if (currentPassword.equals(newPassword)) {
            throw new CustomException(ExceptionType.INVALID_REQUEST);
        }

        if (newPassword.length() < 8 || !newPassword.matches(".*[a-zA-Z].*") || !newPassword.matches(".*\\d.*")) {
            throw new CustomException(ExceptionType.INVALID_REQUEST);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
    }
}
