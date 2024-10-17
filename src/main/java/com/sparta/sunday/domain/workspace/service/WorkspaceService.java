package com.sparta.sunday.domain.workspace.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.entity.AlarmType;
import com.sparta.sunday.domain.alarm.service.AlarmService;
import com.sparta.sunday.domain.common.exception.EntityNotFoundException;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.service.AuthService;
import com.sparta.sunday.domain.workspace.dto.request.ChangeWorkspaceMemeberRoleRequest;
import com.sparta.sunday.domain.workspace.dto.request.InviteWorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.response.ChangeWorkspaceMemeberRoleResponse;
import com.sparta.sunday.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.repository.WorkspaceMemberRepository;
import com.sparta.sunday.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final WorkspaceMemberRepository workspaceMemberRepository;
    private final AuthorizationValidator authorizationValidator;
    private final AuthService authService;
    private final AlarmService alarmService;

    @Transactional
    public void createWorkspace(WorkspaceRequest request, Long userId) {

        User user = authService.findUser(userId);

        authorizationValidator.checkUserAuthorization(user);

        Workspace workspace = workspaceRepository.save(new Workspace(
                user,
                request.getName(),
                request.getDescription()));

        workspaceMemberRepository.save(new WorkspaceMember(
                WorkspaceRole.MANAGER,
                workspace,
                user
        ));
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(
            Long workspaceId,
            WorkspaceRequest request,
            Long userId
    ) {

        Workspace workspace = findWorkspace(workspaceId);

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MANAGER);

        workspace.update(request.getName(), request.getDescription());

        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription()
        );
    }

    public WorkspaceResponse getWorkspace(Long workspaceId, Long userId) {

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("해당 워크스페이스가 존재하지 않습니다."));

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.READ_ONLY);

        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription());
    }

    public Page<WorkspaceResponse> getWorkspaceList(Long userId, int page, int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("updatedAt").descending());

        Page<Workspace> workspaces = workspaceRepository.findByOwnerOrMember(userId, pageable);

        return workspaces.map(workspace -> new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription()
        ));
    }

    public void deleteWorkspace(Long workspaceId, Long userId) {

        User user = authService.findUser(userId);

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("해당 워크스페이스가 존재하지 않습니다."));

        authorizationValidator.checkUserAuthorization(user);

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MANAGER);

        workspaceRepository.delete(workspace);
    }

    @Transactional
    public void inviteMemberToWorkspace(InviteWorkspaceRequest request, Long userId, Long workspaceId) throws SlackApiException, IOException {

        User user = authService.findUser(userId);

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("해당 워크스페이스가 존재하지 않습니다."));

        WorkspaceMember workspaceMember = new WorkspaceMember(
                WorkspaceRole.MEMBER,
                workspace,
                user);

        workspaceMemberRepository.save(workspaceMember);

        for (String email : request.getInviteUserEmailList()) {
            alarmService.saveAlarm(AlarmType.MEMBER, workspaceMember.getId(), user, email);
        }

    }

    public Workspace findWorkspace(Long workspaceId) {
        return workspaceRepository.findById(workspaceId).orElseThrow(
                () -> new EntityNotFoundException("해당 워크스페이스가 존재하지 않습니다."));
    }

    @Transactional
    public ChangeWorkspaceMemeberRoleResponse changeWorkspaceMemberRole(
            Long workspaceId,
            ChangeWorkspaceMemeberRoleRequest changeWorkspaceMemeberRoleRequest,
            Long userId
    ) {

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MANAGER);

        WorkspaceMember member = workspaceMemberRepository.findByMemberIdAndWorkspaceId(changeWorkspaceMemeberRoleRequest.getUserId(), workspaceId);

        member.changeRole(changeWorkspaceMemeberRoleRequest.getRole());

        return new ChangeWorkspaceMemeberRoleResponse(member);
    }

}
