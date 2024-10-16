package com.sparta.sunday.domain.board.entity;

import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Column(name = "img_url")
    private String imgUrl;

    @Column(name = "background_color")
    private String backgroundColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creator_id")
    private User creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id")
    private Workspace workspace;

    public Board(
            User user,
            Workspace workspace,
            String title,
            String content,
            String background,
            String backgroundType
    ) {
        this.creator = user;
        this.workspace = workspace;
        this.title = title;
        this.content = content;

        if (backgroundType.equals("image")) {
            this.imgUrl = background;
        } else if (backgroundType.equals("color")) {
            this.backgroundColor = background;
        }
    }

    public void update(
            String title,
            String content,
            String background,
            String backgroundType
    ) {
        this.title = title;
        this.content = content;

        if (backgroundType.equals("image")) {
            this.imgUrl = background;
            this.backgroundColor = null;
        } else if (backgroundType.equals("color")) {
            this.imgUrl = null;
            this.backgroundColor = background;
        }
    }
}
