package com.sparta.sunday.domain.attachment.entity;
import com.sparta.sunday.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import javax.smartcardio.Card;


@NoArgsConstructor
@Entity
@Getter
@Table(name = "attachments")
public class Attachment extends Timestamped {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String format;

    private int size;

    private String path;

    private String fileName;

    private String creator;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;


    public Attachment(String format,
                      int size,
                      String path,
                      String fileName,
                      String creator,
                      Card card)
    {
        this.format = format;
        this.size = size;
        this.path = path;
        this.fileName = fileName;
        this.creator = creator;
        this.card = card;
    }
}
