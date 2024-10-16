package com.sparta.sunday.domain.attachment.repository;

import com.sparta.sunday.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {

    @Query("SELECT a.fileName FROM Attachment a where a.card.id = :cardId")
    List<String> findAttachmentsByCardId(@Param("cardId") Long cardId);
}
