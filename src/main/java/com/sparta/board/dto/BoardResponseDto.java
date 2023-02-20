package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;
    private Integer likeCount;
    private List<CommentResponseDto> commentList;

    @Builder
    private BoardResponseDto(Board entity) {
        this.id = entity.getId();
        this.title = entity.getTitle();
        this.contents = entity.getContents();
        this.username = entity.getUser().getUsername();
        this.createdAt = entity.getCreatedAt();
        this.modifiedAt = entity.getModifiedAt();
        this.likeCount = entity.getLikesList() != null ? entity.getLikesList().size() : 0;
        this.commentList = entity.getCommentList().stream().map(CommentResponseDto::from).toList();
    }

    public static BoardResponseDto from(Board entity) {
        return BoardResponseDto.builder()
                .entity(entity)
                .build();
    }

}
