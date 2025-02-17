package com.example.newsfeed.user.service;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * 새로운 유저 생성
     */
    @Transactional
    public User createUser(String name, String email, String password) {
        // 이메일 중복 검사
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ExceptionType.DUPLICATE_EMAIL);
        }

        // 비밀번호 암호화 후 저장
        String encodedPassword = passwordEncoder.encode(password);
        User newUser = User.builder()
                .name(name)
                .email(email)
                .password(encodedPassword)
                .build();

        return userRepository.save(newUser);
    }

    /**
     * 특정 유저 조회
     */
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
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

    /**
     * 특정 유저 삭제
     */
    @Transactional
    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ExceptionType.USER_NOT_FOUND));

        userRepository.delete(user);
    }

}
