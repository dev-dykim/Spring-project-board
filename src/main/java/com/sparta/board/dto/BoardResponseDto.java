package com.sparta.board.dto;

import java.time.LocalDateTime;

public class BoardResponseDto {
    private String title;
    private String contents;
    private String author;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BoardResponseDto(String title, String contents, String author, String password, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.password = password;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }
}
