package com.sparta.board.service;

import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.dto.SuccessResponseDto;
import com.sparta.board.entity.User;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional
    public ResponseEntity<SuccessResponseDto> signup(SignupRequestDto requestDto, BindingResult bindingResult) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 입력한 username, password 유효성 검사 통과 못한 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(SuccessResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .msg(bindingResult.getAllErrors().get(0).getDefaultMessage())
                    .build());
        }

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return ResponseEntity.badRequest().body(SuccessResponseDto.builder()
                    .statusCode(HttpStatus.BAD_REQUEST.value())
                    .msg("중복된 사용자가 존재합니다.")
                    .build());
        }

        // 입력한 username, password 로 user 객체 만들어 repository 에 저장
        userRepository.save(User.builder()
                .username(username)
                .password(password)
                .build());

        return ResponseEntity.ok(SuccessResponseDto.builder()
                .statusCode(HttpStatus.OK.value())
                .msg("회원가입 성공")
                .build());

    }
}
