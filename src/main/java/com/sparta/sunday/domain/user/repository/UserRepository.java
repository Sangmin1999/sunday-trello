package com.sparta.sunday.domain.user.repository;

import com.sparta.sunday.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByEmail(String userEmail);
    Optional<User> findByEmail(String email);

}
