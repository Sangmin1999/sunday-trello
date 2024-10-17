package com.sparta.sunday.domain.attachment.dto.response;

import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.Getter;

import java.util.Date;

@Getter
public class GetAttachmentResponse {
    private final String bucketName;
    private final String fileName;
    private final String type;
    private final Date modifiedAt;
    public GetAttachmentResponse(String bucketName, String fileName,String type, Date modifiedAt) {
        this.bucketName = bucketName;
        this.fileName = fileName;
        this.type = type;
        this.modifiedAt = modifiedAt;

    }
}
