package com.sparta.sunday.domain.list.entity;

import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class List extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    @Column(name = "orders")
    private int order;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id")
    private Board board;

    public List(String title, int order, Board board) {
        this.title = title;
        this.order = order;
        this.board = board;
    }
}
