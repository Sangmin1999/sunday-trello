package com.sparta.sunday.domain.comment.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CommentResponse {
    private final Long commentId;
//    private final Long userId;
    private final String content;
}
