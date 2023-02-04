package com.sparta.board.service;

import com.sparta.board.dto.BoardRequestsDto;
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
    public List<Board> getPosts() {
        return boardRepository.findAll();
    }

    @Transactional
    public Board createPost(BoardRequestsDto requestsDto) {
        Board board = new Board(requestsDto);
        boardRepository.save(board);
        return board;
    }
}
