package com.sparta.sunday.domain.attachment.dto.response;

import lombok.Getter;

@Getter
public class UploadAttachmentResponse {
    private final Long id;
    private final String fileName;
    private final String uploader;
    private final String url;

    public UploadAttachmentResponse(Long id, String fileName, String uploader, String url) {
        this.id = id;
        this.fileName = fileName;
        this.uploader = uploader;
        this.url = url;
    }
}
