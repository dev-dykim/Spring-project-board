package com.sparta.board.exception;

import com.sparta.board.entity.enumSet.ErrorType;
import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorType errorType;

    public CustomException(ErrorType errorType) {
        this.errorType = errorType;
    }
}
