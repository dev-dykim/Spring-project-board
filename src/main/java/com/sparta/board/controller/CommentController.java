package com.sparta.board.controller;

import com.sparta.board.dto.CommentRequestDto;
import com.sparta.board.dto.CommentResponseDto;
import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/comment/{id}")   // 여기서 ID는 게시글의 id
    public ResponseEntity<CommentResponseDto> createComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.createComment(id, requestDto, request);
    }

    // 댓글 수정
    @PutMapping("/comment/{id}")    // 여기서 ID는 댓글의 id
    public ResponseEntity<CommentResponseDto> updateComment(@PathVariable Long id, @RequestBody CommentRequestDto requestDto, HttpServletRequest request) {
        return commentService.updateComment(id, requestDto, request);
    }

    // 댓글 삭제
    @DeleteMapping("/comment/{id}")     // 여기서 ID는 댓글의 id
    public ResponseEntity<MessageResponseDto> deleteComment(@PathVariable Long id, HttpServletRequest request) {
        return commentService.deleteComment(id, request);
    }

}
