package com.sparta.sunday.domain.list.entity;

import com.sparta.sunday.domain.board.Board;
import com.sparta.sunday.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "list")
public class List extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
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
