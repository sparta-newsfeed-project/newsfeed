package com.example.newsfeed.auth.controller;

import com.example.newsfeed.auth.consts.SessionConst;
import com.example.newsfeed.auth.dto.AuthRequestDto.LoginRequestDto;
import com.example.newsfeed.auth.dto.AuthResponseDto.LoginResponseDto;
import com.example.newsfeed.auth.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(
            @Valid @RequestBody LoginRequestDto requestDto,
            HttpServletRequest request
    ) {
        LoginResponseDto responseDto = authService.login(requestDto);

        // 세션 생성 및 userId 세션에 저장
        HttpSession session = request.getSession();
        session.setAttribute(SessionConst.LOGIN_USER, responseDto.getUserId());

        return new ResponseEntity<>(responseDto, HttpStatus.OK);
    }
}
