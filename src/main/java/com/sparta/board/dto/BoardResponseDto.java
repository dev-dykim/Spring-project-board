package com.sparta.board.dto;

import java.time.LocalDateTime;

public class BoardResponseDto {
    private Long id;
    private String contents;
    private String author;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
}
