package com.sparta.sunday.domain.comment.entity;

import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comment")
@NoArgsConstructor
public class Comment extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime deletedAt;

//    @ManyToOne
//    @JoinColumn(name = "board_id")
//    private Board board;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public static Comment of(CommentRequest commentRequest, User user) {
        Comment comment = new Comment();
        comment.content = commentRequest.getContent();
        comment.user = user;
        return comment;
    }

    public void update(CommentRequest commentRequest) {
        this.content = commentRequest.getContent();
    }

    public void delete(LocalDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }
}
