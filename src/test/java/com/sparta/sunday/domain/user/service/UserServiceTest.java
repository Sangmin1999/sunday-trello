package com.sparta.sunday.domain.user.service;

import com.sparta.sunday.config.JwtUtil;
import com.sparta.sunday.domain.common.exception.EntityNotFoundException;
import com.sparta.sunday.domain.user.dto.SigninRequest;
import com.sparta.sunday.domain.user.dto.SignupRequest;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    public void 유저_생성_시_이미_사용_중인_이메일() {

        // given

        given(userRepository.existsByEmail(anyString())).willReturn(true);

        SignupRequest signupRequest = new SignupRequest("test1", "password", "test@test.com", "ROLE_USER");

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.signup(signupRequest));

        // then
        assertEquals("이미 존재하는 이메일 입니다.", exception.getMessage());
    }

    @Test
    public void 유저_생성_성공() {

        //given
        SignupRequest signupRequest = new SignupRequest("test1", "password", "test1@aaaa.com", "ROLE_USER");
        given(userRepository.existsByEmail(anyString())).willReturn(false);
        given(passwordEncoder.encode(anyString())).willReturn("password");

        User newUser = new User("test1", "password", "test1@aaaa.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(newUser, "id", 1L);
        when(userRepository.save(any(User.class))).thenReturn(newUser);
        given(jwtUtil.createToken(anyLong(), anyString(), any(UserRole.class))).willReturn("Token");

        //when
        String jwtToken = authService.signup(signupRequest);

        assertEquals("Token", jwtToken);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    public void 로그인_존재하지_않는_유저() {

        //given
        given(userRepository.findByEmail(anyString())).willReturn(Optional.empty());

        // when // then
        SigninRequest signupRequest = new SigninRequest("test1@aaaa.com", "password");
        assertThatThrownBy(() -> authService.signin(signupRequest))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("존재하지 않는 사용자입니다");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    public void 로그인_탈퇴한_유저() {

        //given
        User newUser = new User("test1", "password", "test1@aaaa.com", UserRole.ROLE_USER);
        ReflectionTestUtils.setField(newUser, "id", 1L);
        ReflectionTestUtils.setField(newUser, "signedOut", true);
        given(userRepository.findByEmail(anyString())).willReturn(Optional.of(newUser));

        SigninRequest signupRequest = new SigninRequest("test1@aaaa.com", "password");

        // when // then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> authService.signin(signupRequest));
        assertEquals("이미 탈퇴한 회원입니다.", exception.getMessage());
    }


}
