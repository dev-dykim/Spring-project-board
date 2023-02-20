package com.sparta.board.entity.enumSet;

import lombok.Getter;

@Getter
public enum ErrorType {
    NOT_VALID_TOKEN(400, "토큰이 유효하지 않습니다."),
    NOT_WRITER(400, "작성자만 삭제/수정할 수 있습니다."),
    DUPLICATED_USERNAME(400, "중복된 username 입니다."),
    NOT_MATCHING_INFO(400, "회원을 찾을 수 없습니다."),
    NOT_MATCHING_PASSWORD(400, "비밀번호가 일치하지 않습니다."),
    NOT_FOUND_USER(400, "사용자가 존재하지 않습니다."),
    NOT_FOUND_WRITING(400, "게시글/댓글이 존재하지 않습니다.");

    private int code;
    private String message;

    ErrorType(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
