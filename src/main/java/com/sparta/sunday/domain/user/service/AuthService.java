package com.sparta.sunday.domain.user.service;

import com.sparta.sunday.config.JwtUtil;
import com.sparta.sunday.domain.common.exception.EntityNotFoundException;
import com.sparta.sunday.domain.user.dto.SigninRequest;
import com.sparta.sunday.domain.user.dto.SignoutRequest;
import com.sparta.sunday.domain.user.dto.SignupRequest;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Transactional
    public String signup(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("이미 존재하는 이메일 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        User newUser = new User(
                signupRequest.getUsername(),
                encodedPassword,
                signupRequest.getEmail(),
                userRole
        );

        User savedUser = userRepository.save(newUser);

        return jwtUtil.createToken(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getUserRole()
        );
    }

    public String signin(SigninRequest signinRequest) {

        User user = userRepository.findByEmail(signinRequest.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 사용자입니다"));

        if (user.isSignedOut()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        return jwtUtil.createToken(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );
    }

    @Transactional
    public String signout(Long userId, SignoutRequest signoutRequest) {

        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("해당 유저가 존재하지 않습니다."));

        if (!passwordEncoder.matches(signoutRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (user.isSignedOut()) {
            throw new IllegalArgumentException("이미 탈퇴한 회원입니다.");
        }

        user.signout();

        return "회원 탈퇴가 완료되었습니다.";
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("해당 유저가 존재하지 않습니다."));
    }
}

