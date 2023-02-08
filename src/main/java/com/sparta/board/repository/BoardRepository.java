package com.sparta.board.repository;

import com.sparta.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {
    List<Board> findAllByOrderByModifiedAtDesc();
    List<Board> findAllByOrderByTitleDesc();
    List<Board> findAllByOrderByAuthorDesc();

    Page<Board> findAll(Pageable pageable);
}
