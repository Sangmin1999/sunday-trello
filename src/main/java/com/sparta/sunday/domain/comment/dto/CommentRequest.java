package com.sparta.sunday.domain.comment.dto;

import lombok.Getter;

@Getter
public class CommentRequest {

    private String content;

    public CommentRequest(String content) {
        this.content = content;
    }
}
