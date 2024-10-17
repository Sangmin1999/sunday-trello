package com.sparta.sunday.domain.common.validator;

import com.sparta.sunday.domain.common.exception.UnAuthorizedException;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.repository.WorkspaceMemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class AuthorizationValidatorTest {
    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;

    @InjectMocks
    private AuthorizationValidator authorizationValidator;

    private User mockUser;
    private WorkspaceMember mockWorkspaceMember;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        mockUser = new User(1L, "email@example.com", UserRole.ROLE_ADMIN);
        mockWorkspaceMember = new WorkspaceMember(WorkspaceRole.MANAGER, null, mockUser);
    }

    @Test
    public void checkUserAuthorization_권한없음_예외발생() {
        // Given
        ReflectionTestUtils.setField(mockUser, "userRole", UserRole.ROLE_USER);

        // When / Then
        assertThatThrownBy(() -> authorizationValidator.checkUserAuthorization(mockUser))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessageContaining("해당 기능에 대한 권한이 없습니다.");
    }

    @Test
    public void checkWorkspaceAuthorization_권한없음_예외발생() {
        // Given
        Long userId = 1L;
        Long workspaceId = 1L;

        ReflectionTestUtils.setField(mockWorkspaceMember, "role", WorkspaceRole.MEMBER);

        given(workspaceMemberRepository.findByMemberIdAndWorkspaceId(userId, workspaceId))
                .willReturn(mockWorkspaceMember);

        // When / Then
        assertThatThrownBy(() ->
                authorizationValidator
                        .checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MANAGER))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessageContaining("해당 기능에 대한 권한이 없습니다.");
    }

    @Test
    public void checkWorkspaceMember_멤버아님_예외발생() {
        // Given
        Long userId = 1L;
        Long workspaceId = 1L;
        given(workspaceMemberRepository.findByMemberIdAndWorkspaceId(userId, workspaceId))
                .willReturn(null);

        // When / Then
        assertThatThrownBy(() -> authorizationValidator.checkWorkspaceMember(userId, workspaceId))
                .isInstanceOf(UnAuthorizedException.class)
                .hasMessageContaining("해당 워크스페이스에 속한 멤버가 아닙니다.");
    }

    @Test
    public void checkWorkspaceMember_멤버존재_예외없음() {
        // Given
        Long userId = 1L;
        Long workspaceId = 1L;
        given(workspaceMemberRepository.findByMemberIdAndWorkspaceId(userId, workspaceId))
                .willReturn(mockWorkspaceMember);

        // When
        authorizationValidator.checkWorkspaceMember(userId, workspaceId);

        // Then
        verify(workspaceMemberRepository).findByMemberIdAndWorkspaceId(userId, workspaceId);
    }
}
