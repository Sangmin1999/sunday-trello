package com.sparta.sunday.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class ChangeWorkspaceMemeberRoleResponse {

    private final Long memberId;
    private final String role;

    public ChangeWorkspaceMemeberRoleResponse(Long memberId, String role) {
        this.memberId = memberId;
        this.role = role;
    }
}
