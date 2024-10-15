package com.sparta.sunday.domain.attachment.controller;

import com.sparta.sunday.config.AuthUser;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("/card/{cardId}/attachments")
    public ResponseEntity<UploadAttachmentResponse> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                                     @PathVariable long cardId,
                                                                     @AuthenticationPrincipal AuthUser authUser) {
        return attachmentService.uploadAttachment(file,cardId,authUser);
    }
}
