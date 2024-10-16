package com.sparta.sunday.domain.card.entity;

import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class CardManager extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id")
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private User user;

    public CardManager(Card card, User user) {
        this.card = card;
        this.user = user;
    }

    public String getManagerName() {
        return user.getUsername();
    }
}
