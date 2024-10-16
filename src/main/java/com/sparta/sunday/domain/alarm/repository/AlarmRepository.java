package com.sparta.sunday.domain.alarm.repository;

import com.sparta.sunday.domain.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
}
