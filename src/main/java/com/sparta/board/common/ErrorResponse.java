package com.sparta.board.common;

import com.sparta.board.entity.enumSet.ErrorType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;

@Getter
public class ErrorResponse {
    private int status;
    private String message;

    @Builder
    private ErrorResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public static ErrorResponse of(ErrorType errorType) {
        return ErrorResponse.builder()
                .status(errorType.getCode())
                .message(errorType.getMessage())
                .build();
    }

    public static ErrorResponse of(HttpStatus status, String message) {
        return ErrorResponse.builder()
                .status(status.value())
                .message(message)
                .build();
    }

    public static ErrorResponse of(BindingResult bindingResult) {
        String message = "";

        if (bindingResult.hasErrors()) {
            message = bindingResult.getAllErrors().get(0).getDefaultMessage();
        }

        return ErrorResponse.of(HttpStatus.BAD_REQUEST, message);
    }
}
