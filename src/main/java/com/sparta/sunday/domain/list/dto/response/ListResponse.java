package com.sparta.sunday.domain.list.dto.response;

import com.sparta.sunday.domain.list.entity.List;
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

    public ListResponse(List list) {
        this(
                list.getId(),
                list.getTitle(),
                list.getOrder(),
                list.getBoard().getId()
        );
    }
}
