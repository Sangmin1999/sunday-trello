package com.sparta.sunday.domain.list.repository;

import com.sparta.sunday.domain.list.entity.BoardList;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListRepository extends JpaRepository<BoardList, Long> {
}
