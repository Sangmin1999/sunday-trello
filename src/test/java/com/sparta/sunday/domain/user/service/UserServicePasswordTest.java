package com.sparta.sunday.domain.user.service;

import com.sparta.sunday.config.JwtUtil;
import com.sparta.sunday.domain.user.dto.SigninRequest;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServicePasswordTest {

    @Mock
    private UserRepository userRepository;

    @Spy
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;


    @Test
    public void 로그인_비밀번호_불일치() {

        //given
        String encodedPassword = passwordEncoder.encode("password");
        User newUser = new User("test1", encodedPassword, "test1@aaaa.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(newUser, "id", 1L);
        ReflectionTestUtils.setField(newUser, "signedOut", false);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(newUser));

        SigninRequest signupRequest = new SigninRequest("test1@aaaa.com", "password");

        // when // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.signin(signupRequest));
        assertEquals("비밀번호가 일치하지 않습니다.", exception.getMessage());
    }
}
