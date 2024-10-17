package com.sparta.sunday.domain.list.repository;

import com.sparta.sunday.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ListRepository extends JpaRepository<BoardList, Long> {

    /**
     * COALESCE(MAX(bl.order), 0):
     * MAX(bl.order)는 주어진 보드 내에서 가장 큰 order 값을 반환
     * COALESCE는 MAX(bl.order)가 null일 때(즉, 보드에 리스트가 없을 때) 0을 반환
     */
    // 보드 내에서 최대 order 값을 찾기
    @Query("SELECT COALESCE(MAX(bl.order), 0) FROM BoardList bl WHERE bl.board.id = :boardId")
    Optional<Integer> findMaxOrderByBoardId(@Param("boardId") Long boardId);

    /**
     * bl.board.id = :boardId: 해당 보드(boardId)의 리스트들에 대해서만 순서를 조정
     * bl.order > :deletedOrder: 삭제된 리스트의 order 값보다 큰 리스트들에 대해서만 순서를 감소
     * SET bl.order = bl.order - 1: 해당 조건을 만족하는 리스트들의 order 값을 1씩 감소
     * @param boardId
     * @param deletedOrder
     */
    // 리스트 삭제 후 그 뒤의 리스트들의 순서를 하나씩 감소
    @Modifying
    @Query("UPDATE BoardList bl SET bl.order = bl.order - 1 WHERE bl.board.id = :boardId AND bl.order > :deletedOrder")
    void decrementOrdersAfterDelete(@Param("boardId") Long boardId, @Param("deletedOrder") int deletedOrder);

    // 리스트 순서 변경 시 순서 재정렬
    @Modifying
    @Query("UPDATE BoardList bl SET bl.order = CASE " +
            "WHEN bl.order < :oldOrder AND bl.order >= :newOrder THEN bl.order + 1 " +
            "WHEN bl.order > :oldOrder AND bl.order <= :newOrder THEN bl.order - 1 " +
            "ELSE bl.order END " +
            "WHERE bl.board.id = :boardId")
    void updateListOrder(@Param("boardId") Long boardId, @Param("oldOrder") int oldOrder, @Param("newOrder") int newOrder);
}
/**
 * CASE 문을 사용하여 순서 변경의 두 가지 경우를 처리:
 * WHEN bl.order < :oldOrder AND bl.order >= :newOrder THEN bl.order + 1:
 * 리스트의 순서가 oldOrder보다 작고, newOrder보다 크거나 같을 경우 해당 리스트의 순서를 +1 증가.
 * WHEN bl.order > :oldOrder AND bl.order <= :newOrder THEN bl.order - 1:
 * 리스트의 순서가 oldOrder보다 크고, newOrder보다 작거나 같을 경우 해당 리스트의 순서를 -1 감소.
 * ELSE bl.order: 그 외의 경우에는 순서를 변경하지 않습니다.
 * WHERE 조건:
 * bl.board.id = :boardId: 해당 보드 내에서만 순서를 조정
 */
