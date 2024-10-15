package com.sparta.sunday.domain.user.controller;

import com.sparta.sunday.domain.user.dto.SigninRequest;
import com.sparta.sunday.domain.user.dto.SignupRequest;
import com.sparta.sunday.domain.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<Void> signup(@RequestBody SignupRequest signupRequest) {
        String bearerToken = authService.signup(signupRequest);
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }

    @PostMapping("/auth/signin")
    public ResponseEntity<Void> signin(@RequestBody SigninRequest signinRequest) {
        String bearerToken = authService.signin(signinRequest);
        return ResponseEntity.ok().header("Authorization", bearerToken).build();
    }

}
