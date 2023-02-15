package com.sparta.board.controller;

import com.sparta.board.dto.LoginRequestDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController {

    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<MessageResponseDto> signup(@Valid @RequestBody SignupRequestDto requestDto) {
        return userService.signup(requestDto);
    }

    @PostMapping("/login")
    public ResponseEntity<MessageResponseDto> login(@RequestBody LoginRequestDto requestDto) {
        return userService.login(requestDto);
    }
}
