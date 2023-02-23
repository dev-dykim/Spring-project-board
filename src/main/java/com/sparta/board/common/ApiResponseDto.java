package com.sparta.board.common;

import lombok.Builder;
import lombok.Getter;

@Getter
public class ApiResponseDto<T> {

    private boolean success;
    private T response;
    private ErrorResponse error;

    @Builder
    private ApiResponseDto(boolean success, T response, ErrorResponse error) {
        this.success = success;
        this.response = response;
        this.error = error;
    }

}
