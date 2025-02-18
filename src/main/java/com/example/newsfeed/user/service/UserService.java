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
}
