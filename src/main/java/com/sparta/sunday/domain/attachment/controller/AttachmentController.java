package com.sparta.sunday.domain.attachment.controller;

import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
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

    /*@GetMapping("/card/{cardId}/attachments/{attachmentsId}")
    public ResponseEntity<GetAttachmentPesponse> getAttachment(@PathVariable long cardId,
                                                               @PathVariable long attachmentsId,
                                                               @AuthenticationPrincipal AuthUser authUser){
        return attachmentService.getAttachment(cardId,attachmentsId,authUser);
    }*/

    @PostMapping("/uploadTest")
    public void uploadAttachmentTest(@RequestParam("file") MultipartFile file) {
        attachmentService.uploadAttachmentTest(file);
    }

    @GetMapping("/attachments/{attachmentsId}")
    public void getAttachmentTest(@PathVariable Long attachmentsId) {
        attachmentService.getAttachmentTest(attachmentsId);
    }

}
