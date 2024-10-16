package com.sparta.sunday.domain.attachment.dto.response;

import lombok.Getter;

@Getter
public class UploadAttachmentResponse {

    //private final Long cardId;
    private final String fileName;
    private final Long uploaderId;
    private final String url;

    public UploadAttachmentResponse( /*Long cardId,*/ String fileName , Long uploaderId,  String url) {
        //this.cardId = cardId;
        this.fileName = fileName;
        this.uploaderId = uploaderId;
        this.url = url;
    }
}
