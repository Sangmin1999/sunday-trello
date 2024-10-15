package com.sparta.sunday.domain.board.entity;

import com.sparta.sunday.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "background_color")
    private String backgroundColor;
}
