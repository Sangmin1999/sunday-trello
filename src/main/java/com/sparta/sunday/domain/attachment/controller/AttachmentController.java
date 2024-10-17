package com.sparta.sunday.domain.attachment.controller;

import com.sparta.sunday.domain.attachment.dto.request.DeleateAttachment;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.service.AttachmentService;
import com.sparta.sunday.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping("workspace/{workspaceId}/card/{cardId}/attachments")
    public ResponseEntity<UploadAttachmentResponse> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                                     @PathVariable Long cardId,
                                                                     @PathVariable Long workspaceId,
                                                                     @AuthenticationPrincipal AuthUser authUser) {
        return attachmentService.uploadAttachment(file,cardId,workspaceId,authUser);
    }

    @DeleteMapping("workspace/{workspaceId}/card/{cardId}/attachments")
    public void deleteAttachment(@RequestBody DeleateAttachment deleateAttachment,
                                 @PathVariable Long workspaceId,
                                 @AuthenticationPrincipal AuthUser authUser) {
        attachmentService.deleteAttachment(deleateAttachment,workspaceId,authUser);
    }
}
