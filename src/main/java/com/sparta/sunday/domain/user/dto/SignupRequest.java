package com.sparta.sunday.domain.user.dto;

import lombok.Getter;

@Getter
public class SignupRequest {

    private String username;
    private String password;
    private String email;
    private String userRole;
}
