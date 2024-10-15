package com.sparta.sunday.domain.board.dto.response;

import lombok.Getter;

@Getter
public class BoardResponse {

    private final Long id;
    private final String title;

    public BoardResponse(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
