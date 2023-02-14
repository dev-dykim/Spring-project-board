package com.sparta.board.exception;

import com.sparta.board.dto.MessageResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDto> methodValidException(MethodArgumentNotValidException e) {
        MessageResponseDto responseDto = makeErrorResponse(e.getBindingResult());
        return ResponseEntity.badRequest().body(responseDto);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<MessageResponseDto> commonException(RuntimeException e) {
        MessageResponseDto responseDto = makeErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(responseDto);
    }

    private MessageResponseDto makeErrorResponse(BindingResult bindingResult) {
        String message = "";

        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        return MessageResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(message)
                .build();
    }

    private MessageResponseDto makeErrorResponse(String message) {
        return MessageResponseDto.builder()
                .statusCode(HttpStatus.BAD_REQUEST.value())
                .msg(message)
                .build();
    }
}
