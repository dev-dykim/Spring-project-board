package com.sparta.board.exception;

import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.enumSet.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MessageResponseDto> methodValidException(MethodArgumentNotValidException e) {
        MessageResponseDto responseDto = makeErrorResponse(e.getBindingResult());
        return ResponseEntity.badRequest().body(responseDto);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<MessageResponseDto> commonException(ErrorType error) {
        MessageResponseDto responseDto = makeErrorResponse(error);
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

    private MessageResponseDto makeErrorResponse(ErrorType error) {
        return MessageResponseDto.builder()
                .statusCode(error.getCode())
                .msg(error.getMessage())
                .build();
    }
}
