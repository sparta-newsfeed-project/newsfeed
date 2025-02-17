package com.example.newsfeed.auth.service;

import com.example.newsfeed.auth.config.PasswordEncoder;
import com.example.newsfeed.auth.dto.AuthRequestDto.LoginRequestDto;
import com.example.newsfeed.auth.dto.AuthResponseDto.LoginResponseDto;
import com.example.newsfeed.exception.CustomException;
import com.example.newsfeed.exception.ExceptionType;
import com.example.newsfeed.user.domain.User;
import com.example.newsfeed.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public LoginResponseDto login(LoginRequestDto requestDto) {
        User user = userRepository.findActiveUserByEmail(requestDto.getEmail())
                .orElseThrow(() -> new CustomException(ExceptionType.INVALID_CREDENTIALS));

        validatePasswordMatch(requestDto.getPassword(), user.getPassword());

        return new LoginResponseDto(user.getId());
    }

    private void validatePasswordMatch(String rawPassword, String encodedPassword) {
        if (!passwordEncoder.matches(rawPassword, encodedPassword)) {
            throw new CustomException(ExceptionType.INVALID_CREDENTIALS);
        }
    }
}
