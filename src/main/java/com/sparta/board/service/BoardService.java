package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.SuccessResponseDto;
import com.sparta.board.entity.Board;
import com.sparta.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    @Transactional(readOnly = true)
    public List<BoardResponseDto> getPosts() {
        return boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
    }

    @Transactional
    public BoardResponseDto createPost(BoardRequestsDto requestsDto) {
        Board board = new Board(requestsDto);
        boardRepository.save(board);
        return new BoardResponseDto(board);
    }

    @Transactional
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

    @Transactional
    public SuccessResponseDto deletePost(Long id, BoardRequestsDto requestsDto) throws Exception {
        Board board = getBoardOrElseThrow(id);

        checkPassword(requestsDto, board);

        boardRepository.deleteById(id);
        return new SuccessResponseDto(true);
    }

    private Board getBoardOrElseThrow(Long id) {
        return boardRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
    }

    private static void checkPassword(BoardRequestsDto requestsDto, Board board) throws Exception {
        if (!requestsDto.getPassword().equals(board.getPassword()))
            throw new Exception("비밀번호가 일치하지 않습니다.");
    }

    public List<BoardResponseDto> getPostsOrderBy(String criteria) {
        return switch (criteria) {
            case "title" -> boardRepository.findAllByOrderByTitleDesc().stream().map(BoardResponseDto::new).toList();
            case "author" -> boardRepository.findAllByOrderByAuthorDesc().stream().map(BoardResponseDto::new).toList();
            default -> boardRepository.findAllByOrderByModifiedAtDesc().stream().map(BoardResponseDto::new).toList();
        };
    }
}
