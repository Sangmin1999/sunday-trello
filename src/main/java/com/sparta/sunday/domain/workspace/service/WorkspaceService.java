package com.sparta.sunday.domain.workspace.service;

import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    public void createWorkspace(WorkspaceRequest request) {
        // 권한 확인

        // 생성
        workspaceRepository.save(new Workspace(request.getName(), request.getDescription()));
    }
}
