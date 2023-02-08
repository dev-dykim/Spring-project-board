package com.sparta.board.controller;

import com.sparta.board.dto.BoardRequestsDto;
import com.sparta.board.dto.BoardResponseDto;
import com.sparta.board.dto.SuccessResponseDto;
import com.sparta.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/api/posts")
    public List<BoardResponseDto> getPosts() {
        return boardService.getPosts();
    }

    @PostMapping("/api/post")
    public BoardResponseDto createPost(@RequestBody BoardRequestsDto requestsDto) {
        return boardService.createPost(requestsDto);
    }

    @GetMapping("/api/post/{id}")
    public BoardResponseDto getPost(@PathVariable Long id) {
        return boardService.getPost(id);
    }

    @PutMapping("/api/post/{id}")
    public BoardResponseDto updatePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestsDto) throws Exception {
        return boardService.updatePost(id, requestsDto);
    }

    @DeleteMapping("/api/post/{id}")
    public SuccessResponseDto deletePost(@PathVariable Long id, @RequestBody BoardRequestsDto requestsDto) throws Exception {
        return boardService.deletePost(id, requestsDto);
    }

    @GetMapping("/api/posts-order")
    public List<BoardResponseDto> getPostsOrderBy(@RequestParam(required = false, defaultValue = "modifiedAt", value = "orderby") String criteria) {
        return boardService.getPostsOrderBy(criteria);
    }

    @GetMapping("/api/posts-page")
    public Page<BoardResponseDto> getPostsPage(@RequestParam(required = false, defaultValue = "0", value = "page") int pageNo,
                                               @RequestParam(required = false, defaultValue = "modifiedAt", value = "orderby") String criteria,
                                               @RequestParam(required = false, defaultValue = "DESC", value = "sort") String sort) {
        return boardService.getPostsPage(pageNo, criteria, sort);
    }

}
