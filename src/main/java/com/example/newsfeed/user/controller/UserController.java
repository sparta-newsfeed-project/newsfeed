package com.example.newsfeed.user.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.user.dto.UserRequestDto.ChangePasswordRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.ProfileRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.RegisterRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.WithdrawRequestDto;
import com.example.newsfeed.user.dto.UserResponseDto;
import com.example.newsfeed.user.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Repository
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequestDto requestDto) {
        userService.register(requestDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<Void> withdraw(
            @Authenticated Long userId,
            @Valid @RequestBody WithdrawRequestDto requestDto,
            HttpServletRequest request
    ) {
        userService.withdraw(userId, requestDto);

        HttpSession session = request.getSession(false);
        session.invalidate();

        return new ResponseEntity<>(HttpStatus.OK);
    }

    // 특정 유저 조회
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDto> getUserProfile(@PathVariable Long userId) {
        return new ResponseEntity<>(userService.getUserProfile(userId), HttpStatus.OK);
    }

    // 전체 유저 조회
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers() {
        List<UserResponseDto> users = userService.getAllUsers()
                .stream()
                .map(UserResponseDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    // 프로필 수정
    @PutMapping
    public ResponseEntity<Void> updateProfile(
            @Authenticated Long userId,
            @Valid @RequestBody ProfileRequestDto requestDto
    ) {
        userService.updateProfile(userId, requestDto.getName(), requestDto.getIntroText());
        return ResponseEntity.ok().build();
    }

    // 비밀번호 변경
    @PutMapping("/password")
    public ResponseEntity<Void> changePassword(
            @Authenticated Long userId,
            @Valid @RequestBody ChangePasswordRequestDto requestDto) {

        userService.changePassword(userId, requestDto);
        return ResponseEntity.ok().build();
    }
}
