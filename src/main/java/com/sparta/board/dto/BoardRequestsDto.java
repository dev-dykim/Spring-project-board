package com.sparta.board.dto;

import lombok.Getter;

@Getter
public class BoardRequestsDto {
    private String title;
    private String contents;
    private String author;
    private String password;
}
