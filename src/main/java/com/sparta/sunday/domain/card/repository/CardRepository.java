package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.card.entity.CardManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c " +
            "LEFT JOIN FETCH c.cardManagerList m " +
            "WHERE c.id = :cardId")
    Optional<Card> findCardWithManagers(@Param("cardId") Long cardId);

    @Query("SELECT c FROM Card c WHERE c.id = :cardId")
    Optional<Card> findCardWithDetails(@Param("cardId") Long cardId);

    void deleteByBoardList(BoardList boardList);

    @Query("SELECT c FROM Card c " +
            "JOIN FETCH c.cardManagerList cm " +
            "JOIN FETCH cm.user u " +
            "WHERE (:title IS NULL or c.title like concat('%',:title,'%')) " +
            "AND (:description IS NULL or c.description like concat('%',:description,'%')) " +
            "AND (:dueTo IS NULL or c.dueTo <= :dueTo) " +
            "AND (:manager IS NULL or u.username = :manager)" +
            "ORDER BY c.dueTo")
    Page<Card> search(Pageable pageable,
                      @Param("title") String title,
                      @Param("description") String description,
                      @Param("dueTo") LocalDateTime dueTo,
                      @Param("manager") String manager);

    @Query("SELECT c FROM Card c " +
            "WHERE (c.boardList.board.id = :boardId)" +
            "ORDER BY c.dueTo")
    Page<Card> findCardWithBoardId(Pageable pageable, @Param("boardId") Long boardId);
}
