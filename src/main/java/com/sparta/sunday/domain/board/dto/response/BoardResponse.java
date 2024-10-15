package com.sparta.sunday.domain.board.dto.response;

public class BoardResponse {
    private Long id;
    private String title;
    private String content;
    private String imgUrl;
    private String backgroundColor;

    public BoardResponse(Long id, String title, String content, String imgUrl, String backgroundColor) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.imgUrl = imgUrl;
        this.backgroundColor = backgroundColor;
    }
}
