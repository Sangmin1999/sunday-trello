package com.sparta.sunday.domain.attachment.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GetAttachmentRequest {
    private String bucketName;
    private String fileName;
}
