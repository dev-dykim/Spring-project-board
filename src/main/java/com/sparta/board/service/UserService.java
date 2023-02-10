package com.sparta.board.service;

import com.sparta.board.dto.SuccessResponseDto;
import com.sparta.board.dto.UserRequestDto;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
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
    private final JwtUtil jwtUtil;

    @Transactional
    public ResponseEntity<SuccessResponseDto> signup(UserRequestDto requestDto, BindingResult bindingResult) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 입력한 username, password 유효성 검사 통과 못한 경우
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()  // status : bad request
                    .body(SuccessResponseDto.builder()  // body : SuccessResponseDto (statusCode, msg)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg(bindingResult.getAllErrors().get(0).getDefaultMessage())
                            .build());
        }

        // 회원 중복 확인
        Optional<User> found = userRepository.findByUsername(username);
        if (found.isPresent()) {
            return ResponseEntity.badRequest()  // status : bad request
                    .body(SuccessResponseDto.builder()  // body : SuccessResponseDto (statusCode, msg)
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg("중복된 사용자가 존재합니다.")
                            .build());
        }

        // 입력한 username, password 로 user 객체 만들어 repository 에 저장
        userRepository.save(User.builder()
                .username(username)
                .password(password)
                .build());

        return ResponseEntity.ok(SuccessResponseDto.builder()   // status : ok
                .statusCode(HttpStatus.OK.value())  // body : SuccessResponseDto (statusCode, msg)
                .msg("회원가입 성공")
                .build());

    }

    @Transactional(readOnly = true)
    public ResponseEntity<SuccessResponseDto> login(UserRequestDto requestDto) {
        String username = requestDto.getUsername();
        String password = requestDto.getPassword();

        // 사용자 확인
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.badRequest()  // status : badRequest
                    .body(SuccessResponseDto.builder() // body : SuccessResponseDto -> statusCode, msg
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg("등록된 사용자가 없습니다.")
                            .build());
        }

        // 비밀번호 확인
        if(!user.get().getPassword().equals(password)){
            return ResponseEntity.badRequest()
                    .body(SuccessResponseDto.builder()
                            .statusCode(HttpStatus.BAD_REQUEST.value())
                            .msg("비밀번호가 일치하지 않습니다.")
                            .build());
        }

        // header 에 들어갈 JWT 세팅
        HttpHeaders headers = new HttpHeaders();
        headers.set(JwtUtil.AUTHORIZATION_HEADER, jwtUtil.createToken(user.get().getUsername()));

        return ResponseEntity.ok()  // status -> OK
                .headers(headers)   // headers -> JWT
                .body(SuccessResponseDto.builder() // body -> SuccessResponseDto -> statusCode, msg
                        .statusCode(HttpStatus.OK.value())
                        .msg("로그인 성공")
                        .build());

    }
}
