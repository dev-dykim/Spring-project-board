package com.sparta.board.service;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.entity.enumSet.ErrorType;
import com.sparta.board.entity.enumSet.UserRoleEnum;
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

import static com.sparta.board.exception.ExceptionHandling.responseException;

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

        // Request 에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 없거나 유효하지 않으면 댓글 작성 불가
        if (token == null || !(jwtUtil.validateToken(token)))
            return responseException(ErrorType.NOT_VALID_TOKEN);

        claims = jwtUtil.getUserInfoFromToken(token);

        // 토큰에서 가져온 사용자 정보 DB 조회 (댓글 작성자 확인)
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {   // 토큰에서 가져온 사용자가 DB에 없는 경우
            return responseException(ErrorType.NOT_FOUND_USER);
        }

        // 선택한 게시글 DB 조회
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
            return responseException(ErrorType.NOT_FOUND_WRITING);
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

    // 댓글 수정
    @Transactional
    public ResponseEntity<Object> updateComment(Long id, CommentRequestDto requestDto, HttpServletRequest request) {

        // Request 에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 없거나 유효하지 않으면 댓글 수정 불가
        if (token == null || !(jwtUtil.validateToken(token)))
            return responseException(ErrorType.NOT_VALID_TOKEN);

        claims = jwtUtil.getUserInfoFromToken(token);

        // 토큰에서 가져온 사용자 정보 DB 조회 (댓글 작성자 확인)
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {   // 토큰에서 가져온 사용자가 DB에 없는 경우
            return responseException(ErrorType.NOT_FOUND_USER);
        }

        // 선택한 댓글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            return responseException(ErrorType.NOT_FOUND_WRITING);
        }

        // 댓글의 작성자와 수정하려는 사용자의 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 댓글 수정 가능)
        Optional<Comment> found = commentRepository.findByIdAndUser(id, user.get());
        if (found.isEmpty() && user.get().getRole() == UserRoleEnum.USER) {
            return responseException(ErrorType.NOT_WRITER);
        }

        // 관리자이거나, 댓글의 작성자와 수정하려는 사용자의 정보가 일치한다면, 댓글 수정
        comment.get().update(requestDto, user.get());
        commentRepository.saveAndFlush(comment.get());   // responseDto 에 modifiedAt 업데이트 해주기 위해 saveAndFlush 사용

        // ResponseEntity 에 dto 담아서 리턴
        return ResponseEntity.ok(CommentResponseDto.builder().entity(comment.get()).build());

    }

    // 댓글 삭제
    @Transactional
    public ResponseEntity<Object> deleteComment(Long id, HttpServletRequest request) {
        // Request 에서 Token 가져오기
        String token = jwtUtil.resolveToken(request);
        Claims claims;

        // token 이 없거나 유효하지 않으면 댓글 삭제 불가
        if (token == null || !(jwtUtil.validateToken(token)))
            return responseException(ErrorType.NOT_VALID_TOKEN);

        claims = jwtUtil.getUserInfoFromToken(token);

        // 토큰에서 가져온 사용자 정보 DB 조회 (댓글 작성자 확인)
        Optional<User> user = userRepository.findByUsername(claims.getSubject());
        if (user.isEmpty()) {   // 토큰에서 가져온 사용자가 DB에 없는 경우
            return responseException(ErrorType.NOT_FOUND_USER);
        }

        // 선택한 댓글이 DB에 있는지 확인
        Optional<Comment> comment = commentRepository.findById(id);
        if (comment.isEmpty()) {
            return responseException(ErrorType.NOT_FOUND_WRITING);
        }

        // 댓글의 작성자와 삭제하려는 사용자의 정보가 일치하는지 확인 (삭제하려는 사용자가 관리자라면 댓글 삭제 가능)
        Optional<Comment> found = commentRepository.findByIdAndUser(id, user.get());
        if (found.isEmpty() && user.get().getRole() == UserRoleEnum.USER) {
            return responseException(ErrorType.NOT_WRITER);
        }

        // 관리자이거나, 댓글의 작성자와 삭제하려는 사용자의 정보가 일치한다면, 댓글 삭제
        commentRepository.deleteById(id);

        // ResponseEntity 에 상태코드, 메시지 들어있는 DTO 를 담아서 반환
        return ResponseEntity
                .ok(MessageResponseDto
                        .builder()
                        .msg("댓글 삭제 성공")
                        .statusCode(HttpStatus.OK.value())
                        .build());

    }

}
