package com.sparta.sunday.domain.card.repository;

import com.sparta.sunday.domain.card.entity.CardManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CardManagerRepository extends JpaRepository<CardManager, Long> {
}
