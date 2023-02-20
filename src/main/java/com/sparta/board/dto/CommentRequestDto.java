package com.sparta.board.dto;

import lombok.Getter;

@Getter
public class CommentRequestDto {
    String contents;
    Long parentCommentId;
}
