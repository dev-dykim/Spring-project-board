package com.sparta.board.entity;

import com.sparta.board.dto.UserRequestDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Builder
    public User(UserRequestDto requestsDto) {
        this.username = requestsDto.getUsername();
        this.password = requestsDto.getPassword();
    }
}
