package com.sparta.sunday.domain.attachment.repository;
import com.sparta.sunday.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
}
