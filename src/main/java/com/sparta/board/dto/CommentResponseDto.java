package com.sparta.board.dto;

import com.sparta.board.entity.Comment;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String contents;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer likeCount;

    @Builder
    public CommentResponseDto(Comment entity) {
        this.id = entity.getId();
        this.contents = entity.getContents();
        this.username = entity.getUser().getUsername();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.likeCount = entity.getLikesList() != null ? entity.getLikesList().size() : 0;
    }

    public static CommentResponseDto from(Comment entity) {
        return CommentResponseDto.builder()
                .entity(entity)
                .build();
    }

}
