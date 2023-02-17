package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.entity.Comment;
import com.sparta.board.entity.User;
import com.sparta.board.entity.enumSet.ErrorType;
import com.sparta.board.entity.enumSet.UserRoleEnum;
import com.sparta.board.exception.RestApiException;
import com.sparta.board.jwt.JwtUtil;
import com.sparta.board.repository.BoardRepository;
import com.sparta.board.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    // 게시글 전체 목록 조회
    @Transactional(readOnly = true)
    public ResponseEntity<List<BoardResponseDto>> getPosts() {

        List<Board> boardList = boardRepository.findAllByOrderByModifiedAtDesc();

        // 댓글리스트 작성일자 기준 내림차순 정렬
        for (Board board : boardList) {
            board.getCommentList().sort(Comparator.comparing(Comment::getModifiedAt).reversed());
        }

        List<BoardResponseDto> responseDto = boardList.stream().map(BoardResponseDto::new).toList();

        return ResponseEntity.ok(responseDto);

    }

    // 게시글 작성
    @Transactional
    public ResponseEntity<BoardResponseDto> createPost(BoardRequestsDto requestsDto, User user) {

        // 작성 글 저장
        Board board = boardRepository.save(Board.builder()
                .requestsDto(requestsDto)
                .user(user)
                .build());

        // ResponseEntity 로 반환
        return ResponseEntity.ok(new BoardResponseDto(board));

    }

    // 선택된 게시글 조회
    @Transactional(readOnly = true)
    public ResponseEntity<BoardResponseDto> getPost(Long id) {
        // Id에 해당하는 게시글이 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) { // 해당 게시글이 없다면
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 댓글리스트 작성일자 기준 내림차순 정렬
        board.get().getCommentList().sort(Comparator.comparing(Comment::getModifiedAt).reversed());

        // 해당 게시글이 있다면 게시글 객체를 Dto 로 변환 후, ResponseEntity body 에 담아 리턴
        return ResponseEntity.ok(new BoardResponseDto(board.get()));
    }

    // 선택된 게시글 수정
    @Transactional
    public ResponseEntity<BoardResponseDto> updatePost(Long id, BoardRequestsDto requestsDto, User user) {

        // 선택한 게시글이 DB에 있는지 확인
        Optional<Board> board = boardRepository.findById(id);
        if (board.isEmpty()) {
           throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (수정하려는 사용자가 관리자라면 게시글 수정 가능)
        Optional<Board> found = boardRepository.findByIdAndUser(id, user);
        if (found.isEmpty() && user.getRole() == UserRoleEnum.USER) { // 일치하는 게시물이 없다면
            throw new RestApiException(ErrorType.NOT_WRITER);
        }

        // 게시글 id 와 사용자 정보 일치한다면, 게시글 수정
        board.get().update(requestsDto, user);
        boardRepository.saveAndFlush(board.get());  // responseDto 에 modifiedAt 업데이트 해주기 위해 saveAndFlush 사용

        return ResponseEntity.ok(new BoardResponseDto(board.get()));

    }

    // 게시글 삭제
    @Transactional
    public ResponseEntity<MessageResponseDto> deletePost(Long id, User user) {

        // 선택한 게시글이 DB에 있는지 확인
        Optional<Board> found = boardRepository.findById(id);
        if (found.isEmpty()) {
            throw new RestApiException(ErrorType.NOT_FOUND_WRITING);
        }

        // 선택한 게시글의 작성자와 토큰에서 가져온 사용자 정보가 일치하는지 확인 (삭제하려는 사용자가 관리자라면 게시글 삭제 가능)
        Optional<Board> board = boardRepository.findByIdAndUser(id, user);
        if (board.isEmpty() && user.getRole() == UserRoleEnum.USER) { // 일치하는 게시물이 없다면
            throw new RestApiException(ErrorType.NOT_WRITER);
        }

        // 게시글 id 와 사용자 정보 일치한다면, 게시글 수정
        boardRepository.deleteById(id);
        return ResponseEntity.ok(MessageResponseDto.builder()   // status : 200
                .statusCode(HttpStatus.OK.value())  // body : SuccessResponseDto
                .msg("게시글 삭제 성공")
                .build());

    }

}
