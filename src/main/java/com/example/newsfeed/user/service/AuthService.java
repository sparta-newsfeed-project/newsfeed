package com.example.newsfeed.user.service;

import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.auth.config.PasswordEncoder;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.dto.AuthRequestDto.LoginRequestDto;
import com.example.newsfeed.user.dto.AuthRequestDto.RegisterRequestDto;
import com.example.newsfeed.user.dto.AuthResponseDto.LoginResponseDto;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

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

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findActiveUserByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionType.INVALID_CREDENTIALS));

        validatePasswordMatch(requestDto.getPassword(), user.getPassword());

        return new LoginResponseDto(user.getId());
    }

    private void validateNotDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(ExceptionType.DUPLICATE_EMAIL);
        }
    }

    private void validatePasswordMatch(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ExceptionType.INVALID_CREDENTIALS);
        }
    }
}
