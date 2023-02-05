package com.sparta.board.dto;

import com.sparta.board.entity.Board;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class BoardResponseDto {
    private Long id;
    private String title;
    private String contents;
    private String author;
    private LocalDateTime createdAt;
    private LocalDateTime modifiedAt;

    public BoardResponseDto(Long id, String title, String contents, String author, LocalDateTime createdAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.title = title;
        this.contents = contents;
        this.author = author;
        this.createdAt = createdAt;
        this.modifiedAt = modifiedAt;
    }

    public static BoardResponseDto from(Board entity) {
        return new BoardResponseDto(
                entity.getId(),
                entity.getTitle(),
                entity.getContents(),
                entity.getAuthor(),
                entity.getCreatedAt(),
                entity.getModifiedAt()
        );
    }
}
