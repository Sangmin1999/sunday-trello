package com.sparta.sunday.domain.card.entity;

import com.sparta.sunday.domain.comment.entity.Comment;
import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.list.entity.BoardList;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Card extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    private LocalDateTime dueTo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id")
    private BoardList boardList;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardActivity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardManager> cardManagerList = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardAttachment> cardAttachments = new ArrayList<>();


    public Card(String title, String description, LocalDateTime dueTo, BoardList boardList) {
        this.title = title;
        this.description = description;
        this.dueTo = dueTo;
        this.boardList = boardList;
    }

    public void addManager(CardManager manager) {
        cardManagerList.add(manager);
    }

    public void update(String title, String description, LocalDateTime dueTo) {
        this.title = title;
        this.description = description;
        this.dueTo = dueTo;
    }
}
