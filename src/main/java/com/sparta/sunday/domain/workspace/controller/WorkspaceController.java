package com.sparta.sunday.domain.workspace.controller;

import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/")
    public ResponseEntity<String> createWorkspace(@RequestBody WorkspaceRequest request) {
        workspaceService.createWorkspace(request);
        return ResponseEntity.ok("워크스페이스가 성공적으로 생성되었습니다.");
    }
}
