package com.sparta.sunday.domain.workspace.dto.request;

import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import lombok.Getter;

@Getter
public class ChangeWorkspaceMemeberRoleRequest {

    private Long userId;
    private WorkspaceRole role;
}
