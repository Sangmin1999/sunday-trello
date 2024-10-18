package com.sparta.sunday.domain.board.dto.response;

import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.list.dto.response.ListResponse;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String imgUrl;
    private String backgroundColor;
    private List<ListResponse> lists;

    public BoardResponse(Board board) {
        this.id = board.getId();
        this.title = board.getTitle();
        this.content = board.getContent();
        this.imgUrl = board.getImgUrl();
        this.backgroundColor = board.getBackgroundColor();
        this.lists = board.getLists().stream().map(ListResponse::new).collect(Collectors.toList());
    }
}
