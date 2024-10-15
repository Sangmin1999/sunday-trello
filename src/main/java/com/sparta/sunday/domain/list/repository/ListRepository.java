package com.sparta.sunday.domain.list.repository;

import com.sparta.sunday.domain.list.entity.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<List, Long> {
}
