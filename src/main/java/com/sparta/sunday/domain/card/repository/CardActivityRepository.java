package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.CardActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardActivityRepository extends JpaRepository<CardActivity, Long> {

    @Query("SELECT a FROM CardActivity a " +
            "WHERE a.card.id = :cardId")
    List<CardActivity> findCardActivitiesByCardId(@Param("cardId") Long cardId);

    @Query("SELECT a.action from CardActivity a where a.card.id = :cardId")
    List<String> findGetActivitiesByCardId(@Param("cardId") Long cardId);
}
