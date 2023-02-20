package com.sparta.board.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Likes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private Comment comment;

    @Builder
    public Likes(Board board, User user) {
        this.board = board;
        this.user = user;
    }

    @Builder
    public Likes(Comment comment, User user) {
        this.comment = comment;
        this.user = user;
    }

    public static Likes of(Board board, User user) {
        return Likes.builder()
                .board(board)
                .user(user)
                .build();
    }

    public static Likes of(Comment comment, User user) {
        return new Likes(comment, user);
    }

}
