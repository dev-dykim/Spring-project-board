package com.sparta.board.common;

public class ResponseUtils {

    // 요청 성공인 경우
    public static <T> ApiResponseDto<T> ok(T response) {
        return ApiResponseDto.<T>builder()
                .success(true)
                .response(response)
                .build();
    }

    // 에러 발생한 경우
    public static <T> ApiResponseDto<T> error(ErrorResponse response) {
        return ApiResponseDto.<T>builder()
                .success(false)
                .error(response)
                .build();
    }

}
