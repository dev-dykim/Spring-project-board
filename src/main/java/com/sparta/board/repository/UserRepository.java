package com.sparta.board.repository;

import com.sparta.board.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 중복 회원가입 방지를 위해 같은 이름의 username 있는지 찾는다.
    Optional<User> findByUsername(String username);
}
