package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.CommentRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 댓글 작성
    @Transactional
    public ResponseEntity<Object> createComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {

        // Request에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 없으면 게시글 작성 불가
        if (token != null) {
            // Token 검증
            if (jwtUtil.validateToken(token)) {
                // 토큰에서 사용자 정보 가져오기
                claims = jwtUtil.getUserInfoFromToken(token);
            } else {
                return responseException("토큰이 유효하지 않습니다.");
            }

            // 선택한 게시글 DB 조회
            Optional<Board> board = boardRepository.findById(id);
            if (board.isEmpty()) {
                return responseException("게시글이 존재하지 않습니다.");
            }

            // 토큰에서 가져온 사용자 정보 DB 조회 (댓글 작성자 확인)
            Optional<User> user = userRepository.findByUsername(claims.getSubject());
            if (user.isEmpty()) {   // 토큰에서 가져온 사용자가 DB에 없는 경우
                return responseException("사용자가 존재하지 않습니다.");
            }

            // 게시글이 있다면 댓글 등록
            Comment comment = commentRepository
                    .save(Comment.builder()
                            .requestDto(requestDto)
                            .board(board.get())
                            .user(user.get())
                            .build());

            // ResponseEntity 로 반환
            return ResponseEntity.ok(CommentResponseDto.builder().entity(comment).build());
        }

        // token이 없는 경우
        return responseException("토큰이 유효하지 않습니다.");
    }

    // 예외 경우 처리
    private static ResponseEntity<Object> responseException(String message) {
        return ResponseEntity   // ResponseEntity 를 반환
                .badRequest()   // status : bad request
                .body(MessageResponseDto.builder()  // body : SuccessResponseDto
                        .statusCode(HttpStatus.BAD_REQUEST.value())
                        .msg(message)
                        .build());
    }

}
