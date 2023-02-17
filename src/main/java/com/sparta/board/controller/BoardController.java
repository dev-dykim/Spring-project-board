package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.security.UserDetailsImpl;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 게시글 전체 목록 조회
    @GetMapping("/api/posts")
    public ResponseEntity<List<BoardResponseDto>> getPosts() {
        return boardService.getPosts();
    }

    // 게시글 작성
    @PostMapping("/api/post")
    public ResponseEntity<BoardResponseDto> createPost(@RequestBody BoardRequestsDto requestsDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.createPost(requestsDto, userDetails.getUser());
    }

    // 선택된 게시글 조회
    @GetMapping("/api/post/{id}")
    public ResponseEntity<BoardResponseDto> getPost(@PathVariable Long id) {
        return boardService.getPost(id);
    }

    // 선택된 게시글 수정
    @PutMapping("/api/post/{id}")
    public ResponseEntity<BoardResponseDto> updatePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestsDto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.updatePost(id, requestsDto, userDetails.getUser());
    }

    // 선택된 게시글 삭제
    @DeleteMapping("/api/post/{id}")
    public ResponseEntity<MessageResponseDto> deletePost(@PathVariable Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return boardService.deletePost(id, userDetails.getUser());
    }

}
