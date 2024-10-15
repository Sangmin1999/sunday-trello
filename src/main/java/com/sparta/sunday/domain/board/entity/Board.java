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

    public Board(User user, Workspace workspace, String title, String background, String imageOrColor) {
        this.creator = user;
        this.workspace = workspace;
        this.title = title;

        if(imageOrColor.equals("image")) {
            this.imgUrl = background;
        } else if(imageOrColor.equals("color")) {
            this.backgroundColor = background;
        }
    }
}
