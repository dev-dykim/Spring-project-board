package com.sparta.board.exception;

import com.sparta.board.common.ApiResponseDto;
import com.sparta.board.common.ErrorResponse;
import com.sparta.board.common.ResponseUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> methodValidException(MethodArgumentNotValidException e) {
        ErrorResponse response = ErrorResponse.of(e.getBindingResult());
        return ResponseEntity.badRequest().body(ResponseUtils.error(response));
    }

    @ExceptionHandler(RestApiException.class)
    public ResponseEntity<ApiResponseDto<ErrorResponse>> customException(RestApiException e) {
        ErrorResponse response = ErrorResponse.of(e.getErrorType());
        return ResponseEntity.badRequest().body(ResponseUtils.error(response));
    }

}
