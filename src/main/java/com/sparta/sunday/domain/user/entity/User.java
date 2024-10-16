package com.sparta.sunday.domain.user.entity;

import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.user.enums.UserRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import com.sparta.sunday.domain.common.exception.ServerException;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "user_name", nullable = false)
    private String username;

    @Column(name = "user_password", nullable = false)
    private String password;

    @Column(name = "user_email",unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean signedOut;     // 1. false : 회원가입 상태 2. true : 회원탈퇴 상태

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role", nullable = false)
    private UserRole userRole;

    public User(String username, String password, String email, UserRole userRole) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.signedOut = false;
        this.userRole = userRole;
    }

    public User(Long userId,  String email, UserRole role) {
        this.id = userId;
        this.email = email;
        this.userRole = role;
    }

    public static User fromAuthUser(AuthUser authUser) {
        UserRole role = UserRole.of(
                authUser.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .findFirst()
                        .orElseThrow(() -> new ServerException("권한이 없습니다."))
        );
        return new User(authUser.getUserId(), authUser.getEmail(), role);
    }

    public void signout() {
        this.signedOut = true;
    }
}
