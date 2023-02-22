package com.sparta.board.entity;

import com.sparta.board.dto.CommentRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 2000)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE)
    private List<Likes> likesList = new ArrayList<>();

    @Column
    private Long parentCommentId;

    @OrderBy("createdAt asc ")
    @OneToMany(mappedBy = "parentCommentId", cascade = CascadeType.ALL)
    private List<Comment> childCommentList = new ArrayList<>();

    @Builder
    private Comment(CommentRequestDto requestDto, Board board, User user) {
        this.contents = requestDto.getContents();
        this.parentCommentId = requestDto.getParentCommentId();
        this.board = board;
        this.user = user;
    }

    public void update(CommentRequestDto requestDto, User user) {
        this.contents = requestDto.getContents();
        this.user = user;
    }

    public static Comment of(CommentRequestDto requestDto, Board board, User user) {
        Comment comment = Comment.builder()
                .requestDto(requestDto)
                .board(board)
                .user(user)
                .build();
        board.getCommentList().add(comment);
        return comment;
    }

    public void addChildComment(Comment child) {
        this.getChildCommentList().add(child);
    }
}
