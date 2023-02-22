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
    private Likes(Board board, Comment comment, User user) {
        this.board = board;
        this.comment = comment;
        this.user = user;
    }

    public static Likes of(Board board, User user) {
        Likes likes = Likes.builder()
                .board(board)
                .user(user)
                .build();
        board.getLikesList().add(likes);
        return likes;
    }

    public static Likes of(Comment comment, User user) {
        Likes likes = Likes.builder()
                .comment(comment)
                .user(user)
                .build();
        comment.getLikesList().add(likes);
        return likes;
    }

}
