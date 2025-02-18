package com.example.newsfeed.user.controller;

import com.example.newsfeed.auth.argument.Authenticated;
import com.example.newsfeed.user.dto.UserRequestDto.RegisterRequestDto;
import com.example.newsfeed.user.dto.UserRequestDto.WithdrawRequestDto;
import com.example.newsfeed.user.service.UserService;
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
}
