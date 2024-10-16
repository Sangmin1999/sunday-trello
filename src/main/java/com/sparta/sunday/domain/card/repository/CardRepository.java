package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.Card;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Long> {
}
