package com.sparta.sunday.domain.board.repository;

import com.sparta.sunday.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BoardRepository extends JpaRepository<Board, Long> {
    @Query("SELECT b FROM Board b JOIN FETCH b.workspace w WHERE w.id = :workspaceId")
    Page<Board> findByWorkspaceId(Long workspaceId, Pageable pageable);
}
