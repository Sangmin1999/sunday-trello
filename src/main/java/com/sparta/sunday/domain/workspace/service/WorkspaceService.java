package com.sparta.sunday.domain.workspace.service;

import com.sparta.sunday.domain.workspace.dto.request.InviteWorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public void createWorkspace(WorkspaceRequest request) {
        // 권한 확인

        // 생성
        workspaceRepository.save(new Workspace(request.getName(), request.getDescription()));
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(Long workspaceId, WorkspaceRequest request) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();

        workspace.update(request.getName(), request.getDescription());

        return new WorkspaceResponse(workspace.getId(), workspace.getName(), workspace.getDescription());
    }

    public WorkspaceResponse getWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();
        return new WorkspaceResponse(workspace.getId(), workspace.getName(), workspace.getDescription());
    }

    public Page<WorkspaceResponse> getWorkspaceList(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("updatedAt").descending());
        Page<Workspace> workspaces = workspaceRepository.findAll(pageable);

        return workspaces.map(workspace -> new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription()
        ));
    }

    public void deleteWorkspace(Long workspaceId) {
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow();

        workspaceRepository.delete(workspace);
    }

    public void inviteMemberToWorkspace(InviteWorkspaceRequest request) {
    }
}
