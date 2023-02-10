package com.sparta.board.controller;

import com.sparta.board.dto.SuccessResponseDto;
import com.sparta.board.dto.UserRequestDto;
import com.sparta.board.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
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
    public ResponseEntity<SuccessResponseDto> signup(@Valid @RequestBody UserRequestDto requestDto, BindingResult bindingResult) {
        return userService.signup(requestDto, bindingResult);
    }

    @PostMapping("/login")
    public ResponseEntity<SuccessResponseDto> login(@RequestBody UserRequestDto requestDto) {
        return userService.login(requestDto);
    }
}
