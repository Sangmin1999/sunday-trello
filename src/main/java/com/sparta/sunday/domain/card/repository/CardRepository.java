package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    @Query("SELECT c FROM Card c " +
            "LEFT JOIN FETCH c.cardManagerList m " +
            "WHERE c.id = :cardId")
    Optional<Card> findCardWithManagers(@Param("cardId") Long cardId);

    @Query("SELECT c FROM Card c " +
            "LEFT JOIN FETCH c.cardManagerList m " +
            "LEFT JOIN FETCH c.activities a " +
            "LEFT JOIN FETCH c.comments cm " +  // 댓글 추가
            "LEFT JOIN FETCH c.attachments at " + // 첨부파일 추가
            "WHERE c.id = :cardId")
    Optional<Card> findCardWithDetails(@Param("cardId") Long cardId);
}
