package com.sparta.sunday.domain.attachment.service;

import com.sparta.sunday.domain.attachment.repository.AttachmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {
    private final AttachmentRepository attachmentRepository;
}
