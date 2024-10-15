package com.sparta.sunday.domain.list.dto.response;

import lombok.Getter;

@Getter
public class ListResponse {

    private final Long id;
    private final String title;
    private final int order;
    private final Long boardId;

    public ListResponse(Long id, String title, int order, Long boardId) {
        this.id = id;
        this.title = title;
        this.order = order;
        this.boardId = boardId;
    }
}
