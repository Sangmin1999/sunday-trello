package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.CardAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardAttachmentRepository extends JpaRepository<CardAttachment, Long> {

    @Query("SELECT a.fileName FROM CardAttachment a where a.card.id = :cardId")
    List<String> findAttachmentsByCardId(@Param("cardId") Long cardId);
}
