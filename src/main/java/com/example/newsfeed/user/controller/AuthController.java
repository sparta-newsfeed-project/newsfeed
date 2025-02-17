package com.example.newsfeed.user.controller;

import com.example.newsfeed.user.dto.AuthRequestDto.RegisterRequestDto;
import com.example.newsfeed.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class AuthController {

    private final AuthService authService;

    @PostMapping("users/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        authService.register(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
