package com.sparta.sunday.domain.attachment.dto.response;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;

@Getter
public class GetAttachmentResponse {
    private final String bucketName;
    private final String fileName;
    private final String type;
    public GetAttachmentResponse(String bucketName, String fileName,String type) {
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.type = type;

    }
}
