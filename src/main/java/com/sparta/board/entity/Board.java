package com.sparta.board.entity;

import com.sparta.board.dto.BoardRequestsDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Getter
@Entity
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String contents;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public Board(BoardRequestsDto requestsDto, User user) {
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.user = user;
    }

    public void update(BoardRequestsDto requestsDto, User user) {
        this.title = requestsDto.getTitle();
        this.contents = requestsDto.getContents();
        this.user = user;
    }
}
