package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.User;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.UserRepository;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 게시글 전체 목록 조회
    @Transactional(readOnly = true)
    public List<BoardResponseDto> getPosts() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    // 게시글 작성
    @Transactional
    public BoardResponseDto createPost(BoardRequestsDto requestsDto, HttpServletRequest request) {

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
                throw new IllegalArgumentException("Token Error");
            }

            // 토큰에서 가져온 사용자 정보를 사용하여 DB 조회
            User user = userRepository.findByUsername(claims.getSubject()).orElseThrow(
                    () -> new IllegalArgumentException("사용자가 존재하지 않습니다.")
            );

            // 게시글 저장 후 responseDto 로 담아서 반환
            return new BoardResponseDto(boardRepository.save(Board.builder()
                            .title(requestsDto.getTitle())
                            .contents(requestsDto.getContents())
                            .user(user)
                            .build()));
        }
        return null;
    }

    // 선택된 게시글 조회
    @Transactional(readOnly = true)
    public BoardResponseDto getPost(Long id) {
        return new BoardResponseDto(getBoardOrElseThrow(id));
    }

    @Transactional
    public BoardResponseDto updatePost(Long id, BoardRequestsDto requestsDto) throws Exception {
        Board board = getBoardOrElseThrow(id);

        checkPassword(requestsDto, board);

        board.update(requestsDto);
        return new BoardResponseDto(board);
    }

//    @Transactional
//    public SuccessResponseDto deletePost(Long id, BoardRequestsDto requestsDto) throws Exception {
//        Board board = getBoardOrElseThrow(id);
//
//        checkPassword(requestsDto, board);
//
//        boardRepository.deleteById(id);
//        return new SuccessResponseDto(true);
//    }

    private Board getBoardOrElseThrow(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
    }

    private static void checkPassword(BoardRequestsDto requestsDto, Board board) throws Exception {
//        if (!requestsDto.getPassword().equals(board.getPassword()))
//            throw new Exception("비밀번호가 일치하지 않습니다.");
    }
}
