package com.sparta.board.entity;

import com.sparta.board.dto.SignupRequestDto;
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

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(SignupRequestDto requestsDto) {
        this.username = requestsDto.getUsername();
        this.password = requestsDto.getPassword();

        // admin = true 로 입력했을 경우
        if (requestsDto.getAdmin()) {
            this.role = UserRoleEnum.ADMIN;
        } else {  // admin = false 로 입력했을 경우
            this.role = UserRoleEnum.USER;
        }
    }

}
