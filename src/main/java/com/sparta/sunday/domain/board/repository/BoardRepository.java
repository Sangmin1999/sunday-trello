package com.sparta.sunday.domain.board.repository;

import com.sparta.sunday.domain.board.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {
}
