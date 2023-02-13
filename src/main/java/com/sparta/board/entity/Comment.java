package com.sparta.board.entity;

import com.sparta.board.dto.CommentRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Comment(CommentRequestDto requestDto, Board board, User user) {
        this.contents = requestDto.getContents();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }
}
