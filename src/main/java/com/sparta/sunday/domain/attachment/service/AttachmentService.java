package com.sparta.sunday.domain.attachment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.repository.AttachmentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.attachment.entity.Attachment;
import com.sparta.sunday.domain.common.exception.InvalidRequestException;
import com.sparta.sunday.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final AttachmentRepository attachmentRepository;
    //private final CardRepository cardRepository;
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    @Value("${cloud.aws.region.static}")
    private String region;

    public ResponseEntity<UploadAttachmentResponse> uploadAttachment(MultipartFile file, Long cardId, AuthUser authUser) {

        User user = User.fromAuthUser(authUser);
        /*Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new InvalidRequestException("Card not found"));*/
        String fileName = makeFileName(file);
        String URL = makeFileUrl(fileName,bucketName,region);
        uploadFile(file,bucketName);
        UploadAttachmentResponse uploadAttachmentResponse = new UploadAttachmentResponse(
                fileName,
                user.getId(),
                URL
        );

        return ResponseEntity.ok(uploadAttachmentResponse);
    }

    /*--------------------------------------------------util---------------------------------------------------------------*/
    public String makeFileName(MultipartFile file){
        return UUID.randomUUID() + file.getOriginalFilename();
    }

    public String makeFileUrl(String fileName, String bucketName, String region){
        return "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
    }

    public void uploadFile(MultipartFile file,String bucketName) {

        String fileName = makeFileName(file);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void getAttachmentTest(Long attachmentsId) {

        Attachment attachment = attachmentRepository.findById(attachmentsId).orElseThrow(()->
                new InvalidRequestException("Card not found"));
        String fileName = attachment.getFileName();
        System.out.println(fileName);
        System.out.println(attachment.getUploaderId());
        System.out.println(attachment.getPath());
        System.out.println(attachment.getFormat());
        System.out.println(attachment.getSize());
    }
}
