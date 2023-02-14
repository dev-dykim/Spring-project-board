package com.sparta.board.entity;

import com.sparta.board.dto.SignupRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 15)
    private String username;

    @Column(nullable = false, length = 20)
    private String password;

    @Column(nullable = false, length = 10)
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
