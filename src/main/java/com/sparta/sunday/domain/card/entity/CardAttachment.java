package com.sparta.sunday.domain.card.entity;
import com.sparta.sunday.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Entity
@Getter
@Table(name = "attachments")
public class CardAttachment extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String format;

    private Long size;

    private String path;

    private String fileName;

    private Long uploaderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    public CardAttachment(String format, Long size, String path, String fileName, Long uploaderId, Card card) {
        this.format = format;
        this.size = size;
        this.path = path;
        this.fileName = fileName;
        this.uploaderId = uploaderId;
        this.card = card;
    }

}
