package com.sparta.board.exception;

import com.sparta.board.dto.MessageResponseDto;
import com.sparta.board.entity.enumSet.ErrorType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<MessageResponseDto> customException(RestApiException e) {
        MessageResponseDto responseDto = makeErrorResponse(e.getErrorType());
        return ResponseEntity.badRequest().body(responseDto);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<MessageResponseDto> usernameFoundException() {
        MessageResponseDto responseDto = makeErrorResponse(ErrorType.NOT_FOUND_USER);
        return ResponseEntity.badRequest().body(responseDto);
    }

    private MessageResponseDto makeErrorResponse(BindingResult bindingResult) {
        String message = "";

        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        return MessageResponseDto.of(HttpStatus.BAD_REQUEST, message);

    }

    private MessageResponseDto makeErrorResponse(ErrorType error) {
        return MessageResponseDto.of(error.getCode(), error.getMessage());
    }
}
