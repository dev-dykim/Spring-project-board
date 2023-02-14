package com.sparta.board.exception;

import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.enumSet.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ExceptionHandling {

    public static ResponseEntity<Object> responseException(ErrorType error) {
        MessageResponseDto responseDto = MessageResponseDto.builder()
                .statusCode(error.getCode())
                .msg(error.getMessage())
                .build();

        return ResponseEntity.badRequest().body(responseDto);   // ResponseEntity 에 status : bad request, body : responseDto 넣어서 반환
    }

    public static ResponseEntity<Object> responseException(HttpStatus status, String message) {
        MessageResponseDto responseDto = MessageResponseDto.builder()
                .statusCode(status.value())
                .msg(message)
                .build();

        return ResponseEntity.badRequest().body(responseDto);   // ResponseEntity 에 status : bad request, body : responseDto 넣어서 반환
    }
}
