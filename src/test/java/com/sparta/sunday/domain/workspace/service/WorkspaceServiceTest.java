/*
package com.sparta.sunday.domain.workspace.service;

import com.sparta.sunday.domain.common.exception.EntityNotFoundException;
import com.sparta.sunday.domain.common.exception.UnAuthorizedException;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.user.service.AuthService;
import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.repository.WorkspaceMemberRepository;
import com.sparta.sunday.domain.workspace.repository.WorkspaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkspaceServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkspaceRepository workspaceRepository;

    @Mock
    private WorkspaceMemberRepository workspaceMemberRepository;

    @Mock
    private AuthorizationValidator authorizationValidator;

    @Mock
    private AuthService authService;

    @InjectMocks
    private WorkspaceService workspaceService;

    private User mockUser;
    private WorkspaceRequest mockRequest;

    private Workspace mockWorkspace;

    @BeforeEach
    public void setup() {
        mockUser = new User(1L, "email@example.com", UserRole.ROLE_ADMIN);

        mockRequest = new WorkspaceRequest();
        ReflectionTestUtils.setField(mockRequest, "name", "workspace name");
        ReflectionTestUtils.setField(mockRequest, "description", "description");

        mockWorkspace = new Workspace();
        ReflectionTestUtils.setField(mockWorkspace, "id", 1L);
        ReflectionTestUtils.setField(mockWorkspace, "name", "workspace name");
        ReflectionTestUtils.setField(mockWorkspace, "description", "description");
    }

    @Test
    public void 워크스페이스_생성_성공() {
        // Given
        given(authService.findUser(any(Long.class))).willReturn(mockUser);
        given(workspaceRepository.save(any(Workspace.class))).willAnswer(invocation -> invocation.getArgument(0));

        // When
        workspaceService.createWorkspace(mockRequest, 1L);

        // Then
        verify(authService).findUser(1L);
        verify(workspaceRepository).save(any(Workspace.class));
        verify(workspaceMemberRepository).save(any(WorkspaceMember.class));
    }

    @Test
    public void 워크스페이스_생성_존재하지않는유저() {
        // Given
        given(authService.findUser(any(Long.class))).willThrow(new EntityNotFoundException("해당 유저가 존재하지 않습니다."));

        // When/Then
        assertThatThrownBy(() -> workspaceService.createWorkspace(mockRequest, 1L))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining("해당 유저가 존재하지 않습니다.");

        then(authService).should().findUser(1L);
        then(workspaceRepository).shouldHaveNoInteractions();
    }

    @Test
    public void 워크스페이스_업데이트_성공() {
        // Given
        Long workspaceId = 1L;
        Long userId = 1L;

        WorkspaceRequest mockUpdateRequest = new WorkspaceRequest();
        ReflectionTestUtils.setField(mockUpdateRequest, "name", "Updated workspace name");
        ReflectionTestUtils.setField(mockUpdateRequest, "description", "Updated description");

        given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(mockWorkspace));

        // When
        WorkspaceResponse response = workspaceService.updateWorkspace(workspaceId, mockUpdateRequest, userId);

        // Then
        assertThat(response.getName()).isEqualTo("Updated workspace name");
        assertThat(response.getDescription()).isEqualTo("Updated description");

        then(authorizationValidator).should().checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MANAGER);

        assertThat(mockWorkspace.getName()).isEqualTo("Updated workspace name");
        assertThat(mockWorkspace.getDescription()).isEqualTo("Updated description");
    }

    @Test
    public void 워크스페이스_조회_성공() {
        // Given
        given(workspaceRepository.findById(1L)).willReturn(Optional.of(mockWorkspace));
        doNothing().when(authorizationValidator).checkWorkspaceAuthorization(1L, 1L, WorkspaceRole.READ_ONLY);

        // When
        WorkspaceResponse response = workspaceService.getWorkspace(1L, 1L);

        // Then
        assertThat(response.getId()).isEqualTo(mockWorkspace.getId());
        assertThat(response.getName()).isEqualTo(mockWorkspace.getName());
        assertThat(response.getDescription()).isEqualTo(mockWorkspace.getDescription());

        then(workspaceRepository).should().findById(1L);
        then(authorizationValidator).should().checkWorkspaceAuthorization(1L, 1L, WorkspaceRole.READ_ONLY);
    }

    @Test
    public void 워크스페이스_목록_조회_성공() {
        // Given
        Pageable pageable = PageRequest.of(0, 10, Sort.by("updatedAt").descending());
        Page<Workspace> workspacePage = new PageImpl<>(Collections.singletonList(mockWorkspace), pageable, 1);
        given(workspaceRepository.findByOwnerOrMember(eq(1L), any(Pageable.class))).willReturn(workspacePage);

        // When
        Page<WorkspaceResponse> response = workspaceService.getWorkspaceList(1L, 0, 10);

        // Then
        then(workspaceRepository).should().findByOwnerOrMember(eq(1L), any(Pageable.class));
        assertEquals(1, response.getTotalElements());
        assertEquals("workspace name", response.getContent().get(0).getName());
    }

    @Test
    public void 워크스페이스_삭제_성공() {
        // Given
        Long workspaceId = 1L;
        Long userId = 1L;
        User mockUser = new User(1L, "email@example.com", UserRole.ROLE_ADMIN);

        given(authService.findUser(userId)).willReturn(mockUser);
        given(workspaceRepository.findById(workspaceId)).willReturn(Optional.of(mockWorkspace));

        // When
        workspaceService.deleteWorkspace(workspaceId, userId);

        // Then
        then(authService).should().findUser(userId);
        then(workspaceRepository).should().findById(workspaceId);
        then(authorizationValidator).should().checkUserAuthorization(mockUser);
        then(authorizationValidator).should().checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MANAGER);
        then(workspaceRepository).should().delete(mockWorkspace);
    }
}
*/
