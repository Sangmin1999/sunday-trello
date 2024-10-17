package com.sparta.sunday.domain.attachment.controller;

import com.sparta.sunday.domain.attachment.dto.request.DeleateAttachment;
import com.sparta.sunday.domain.attachment.dto.request.GetAttachmentRequest;
import com.sparta.sunday.domain.attachment.dto.response.GetAttachmentResponse;
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
@RequestMapping("workspace/{workspaceId}/card/{cardId}/attachments")
public class AttachmentController {
    private final AttachmentService attachmentService;

    @PostMapping()
    public ResponseEntity<UploadAttachmentResponse> uploadAttachment(@RequestParam("file") MultipartFile file,
                                                                     @PathVariable Long cardId,
                                                                     @PathVariable Long workspaceId,
                                                                     @AuthenticationPrincipal AuthUser authUser) {
        return attachmentService.uploadAttachment(file,cardId,workspaceId,authUser);
    }

    @GetMapping
    public ResponseEntity<GetAttachmentResponse> getAttachment(@RequestBody GetAttachmentRequest getAttachmentRequest){
        return attachmentService.getAttachment(getAttachmentRequest);
    }

    @DeleteMapping()
    public ResponseEntity<String> deleteAttachment(@RequestBody DeleateAttachment deleateAttachment,
                                 @PathVariable Long workspaceId,
                                 @AuthenticationPrincipal AuthUser authUser) {
        attachmentService.deleteAttachment(deleateAttachment,workspaceId,authUser);
        return ResponseEntity.ok("첨부파일이 성공적으로 삭제되었습니다.");
    }
}
