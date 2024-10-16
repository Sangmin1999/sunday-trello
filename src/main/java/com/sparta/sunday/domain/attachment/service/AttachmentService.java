package com.sparta.sunday.domain.attachment.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.sparta.sunday.config.AuthUser;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.entity.Attachment;
import com.sparta.sunday.domain.attachment.repository.AttachmentRepository;
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

import javax.smartcardio.Card;
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


    /*@Transactional
    public ResponseEntity<UploadAttachmentResponse> uploadAttachment(MultipartFile file, Long cardId, AuthUser authUser) {
        try {
            User user = User.fromAuthUser(authUser);
            Card card = cardRepository.findById(cardId).orElseThrow(() ->
                    new InvalidRequestException("Card not found"));

            String fileName = UUID.randomUUID() + file.getOriginalFilename();
            String fileUrl = "https://" + bucketName + ".s3." + "ap-northeast-2" + ".amazonaws.com/" + fileName;

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);

            Attachment attachment = new Attachment(
                    file.getContentType(),
                    file.getSize(),
                    fileUrl,
                    file.getName(),
                    user.getUsername(),
                    card
            );

            attachmentRepository.save(attachment);
            UploadAttachmentResponse uploadAttachmentResponse = new UploadAttachmentResponse(
                    attachment.getId(),
                    attachment.getFileName(),
                    attachment.getUploader(),
                    attachment.getPath()
            );

            return ResponseEntity.ok(uploadAttachmentResponse);

        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }*/


    public void uploadAttachmentTest(MultipartFile file) {
        try
        {
        String fileName = UUID.randomUUID() + file.getOriginalFilename();
        String fileUrl = "https://" + bucketName + ".s3." + "ap-northeast-2" + ".amazonaws.com/" + fileName;

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        System.out.println("들어가기 전");
        amazonS3Client.putObject(bucketName, fileUrl, file.getInputStream(), metadata);
        System.out.println("나왔다");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
