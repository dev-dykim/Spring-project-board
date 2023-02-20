package com.sparta.board.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class MessageResponseDto {
    private String msg;
    private int statusCode;

    @Builder
    private MessageResponseDto(String msg, int statusCode) {
        this.msg = msg;
        this.statusCode = statusCode;
    }

    // HttpStatus 상태 입력으로 Dto 만들기
    public static MessageResponseDto of(HttpStatus status, String msg) {
        return MessageResponseDto.builder()
                .statusCode(status.value())
                .msg(msg)
                .build();
    }

    // StatusCode 입력으로 Dto 만들기
    public static MessageResponseDto of(int statusCode, String msg) {
        return MessageResponseDto.builder()
                .statusCode(statusCode)
                .msg(msg)
                .build();
    }
}
