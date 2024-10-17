package com.sparta.sunday.domain.common.validator;

import com.sparta.sunday.domain.common.exception.UnAuthorizedException;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.repository.WorkspaceMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorizationValidator {

    private final WorkspaceMemberRepository workspaceMemberRepository;

    public void checkUserAuthorization(User user) {

        if (!user.getUserRole().equals(UserRole.ROLE_ADMIN)) {
            throw new UnAuthorizedException("해당 기능에 대한 권한이 없습니다.");
        }
    }

    public void checkWorkspaceAuthorization(Long userId, Long workspaceId, WorkspaceRole role) {

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(userId, workspaceId);

        if (!(workspaceMember.getRole().getValue() >= role.getValue())) {
            throw new UnAuthorizedException("해당 기능에 대한 권한이 없습니다.");
        }
    }

    public void checkWorkspaceMember(Long userId, Long workspaceId) {

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(userId, workspaceId);

        if(workspaceMember == null) {
            throw new UnAuthorizedException("해당 워크스페이스에 속한 멤버가 아닙니다.");
        }
    }
}
