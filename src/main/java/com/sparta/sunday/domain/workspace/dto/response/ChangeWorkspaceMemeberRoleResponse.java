package com.sparta.sunday.domain.workspace.dto.response;

import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import lombok.Getter;

@Getter
public class ChangeWorkspaceMemeberRoleResponse {

    private final Long memberId;
    private final WorkspaceRole role;

    public ChangeWorkspaceMemeberRoleResponse(WorkspaceMember member) {
        this.memberId = member.getMember().getId();
        this.role = member.getRole();
    }
}
