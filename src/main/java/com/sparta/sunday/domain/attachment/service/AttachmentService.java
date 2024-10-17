package com.sparta.sunday.domain.attachment.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.S3Object;
import com.sparta.sunday.domain.attachment.dto.request.DeleateAttachment;
import com.sparta.sunday.domain.attachment.dto.request.GetAttachmentRequest;
import com.sparta.sunday.domain.attachment.dto.response.GetAttachmentResponse;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.exception.InvalidRequestException;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.common.validator.FileValidator;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {

    private final CardRepository cardRepository;
    private final AmazonS3Client amazonS3Client;
    private final AuthorizationValidator authorizationValidator;
    private final FileValidator fileValidator;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public ResponseEntity<UploadAttachmentResponse> uploadAttachment(MultipartFile file, Long cardId,Long workspaceId, AuthUser authUser) {

        // 파일 크기 확인 -> 파일 형식 확인 -> 유저 권한 확인
        List<String> acceptedTypes = Arrays.asList("jpg", "png","pdf", "csv");
        fileValidator.fileSizeValidator(file, 5*1000000L);
        fileValidator.fileTypeValidator(file,acceptedTypes);
        authorizationValidator.checkWorkspaceAuthorization(authUser.getUserId(),workspaceId, WorkspaceRole.MEMBER);

        User user = User.fromAuthUser(authUser);
        Card card = cardRepository.findById(cardId).orElseThrow(() ->
                new InvalidRequestException("Card not found"));
        uploadFile(file,bucketName);
        URL url = getUrl(bucketName, file.getOriginalFilename());
        UploadAttachmentResponse uploadAttachmentResponse = new UploadAttachmentResponse(
                card.getId(),
                file.getOriginalFilename(),
                user.getId(),
                url
        );
        return ResponseEntity.ok(uploadAttachmentResponse);
    }

    public ResponseEntity<GetAttachmentResponse> getAttachment(GetAttachmentRequest getAttachmentRequest) {
        try{
            S3Object object =amazonS3Client.getObject(getAttachmentRequest.getBucketName(),getAttachmentRequest.getFileName());
            GetAttachmentResponse getAttachmentResponse = new GetAttachmentResponse(object.getBucketName(),
                    object.getKey(),
                    object.getObjectMetadata().getContentType(),
                    object.getObjectMetadata().getLastModified());
            return ResponseEntity.ok(getAttachmentResponse);

        } catch (AmazonServiceException e){
            throw new InvalidRequestException("Could not get attachment");
        }
    }

    public void deleteAttachment(DeleateAttachment deleateAttachment,
                                 Long workspaceId,
                                 AuthUser authUser) {
        authorizationValidator.checkWorkspaceAuthorization(authUser.getUserId(),workspaceId, WorkspaceRole.MEMBER);
        try {
            amazonS3Client.deleteObject(new DeleteObjectRequest(deleateAttachment.getBucketName(), deleateAttachment.getFileName()));

        } catch (AmazonServiceException e){
            throw new InvalidRequestException("Could not delete attachment");
        }
    }


    /*--------------------------------------------------util---------------------------------------------------------------*/
    public String makeFileName(MultipartFile file){
        return UUID.randomUUID() + file.getOriginalFilename();

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

    // 버킷 이름과 오리지널 파일이름으로 URL 반환하는 메서드
    public URL getUrl(String bucketName, String orginalFileName) {
       return amazonS3Client.getUrl(bucketName, orginalFileName);
    }

}
