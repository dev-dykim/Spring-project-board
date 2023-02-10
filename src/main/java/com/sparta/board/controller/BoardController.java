package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 목록 조회
    @GetMapping("/api/posts")
    public List<BoardResponseDto> getPosts() {
        return boardService.getPosts();
    }

    // 게시글 작성
    @PostMapping("/api/post")
    public BoardResponseDto createPost(@RequestBody BoardRequestsDto requestsDto, HttpServletRequest request) {
        return boardService.createPost(requestsDto, request);
    }

    // 선택된 게시글 조회
    @GetMapping("/api/post/{id}")
    public BoardResponseDto getPost(@PathVariable Long id) {
        return boardService.getPost(id);
    }

    // 선택된 게시글 수정
    @PutMapping("/api/post/{id}")
    public BoardResponseDto updatePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestsDto, HttpServletRequest request) {
        return boardService.updatePost(id, requestsDto, request);
    }

//    @DeleteMapping("/api/post/{id}")
//    public SuccessResponseDto deletePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestsDto) throws Exception {
//        return boardService.deletePost(id, requestsDto);
//    }

}
