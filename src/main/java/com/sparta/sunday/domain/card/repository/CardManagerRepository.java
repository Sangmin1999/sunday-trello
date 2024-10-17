package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.CardManager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardManagerRepository extends JpaRepository<CardManager, Long> {

    @Query("SELECT cm FROM CardManager cm WHERE cm.card.id = :cardId")
    List<CardManager> findCardManagersByCardId(@Param("cardId") Long cardId);
}
