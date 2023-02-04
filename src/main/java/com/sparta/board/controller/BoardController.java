package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.entity.Board;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/posts")
    public List<Board> getPosts() {
        return boardService.getPosts();
    }

    @PostMapping("/api/post")
    public Board createPost(@RequestBody BoardRequestsDto requestsDto) {
        return boardService.createPost(requestsDto);
    }

}
