package com.sparta.sunday.domain.user.repository;

import com.sparta.sunday.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
