package com.sparta.board.entity;

import com.sparta.board.dto.SignupRequestDto;
import com.sparta.board.entity.enumSet.UserRoleEnum;
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

    @Column(nullable = false, length = 20)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, length = 30)
    @Enumerated(value = EnumType.STRING)
    private UserRoleEnum role;

    @Builder
    public User(SignupRequestDto requestsDto, String password) {
        this.username = requestsDto.getUsername();
        this.password = password;

        // admin = true 로 입력했을 경우
        if (requestsDto.getAdmin()) {
            this.role = UserRoleEnum.ADMIN;
        } else {  // admin = false 로 입력했을 경우
            this.role = UserRoleEnum.USER;
        }
    }

    public static User of(SignupRequestDto requestDto, String password) {
        return User.builder()
                .requestsDto(requestDto)
                .password(password)
                .build();
    }

}
