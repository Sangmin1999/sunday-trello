package com.sparta.sunday.domain.workspace.dto.request;

import lombok.Getter;

@Getter
public class ChangeWorkspaceMemeberRoleRequest {

    private Long userId;
    private String role;
}
