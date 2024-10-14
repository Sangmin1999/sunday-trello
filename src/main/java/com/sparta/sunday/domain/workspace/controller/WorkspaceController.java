package com.sparta.sunday.domain.workspace.controller;

import com.sparta.sunday.domain.workspace.dto.request.InviteWorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.sunday.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("")
    public ResponseEntity<String> createWorkspace(@RequestBody WorkspaceRequest request) {
        workspaceService.createWorkspace(request);
        return ResponseEntity.ok("워크스페이스가 성공적으로 생성되었습니다.");
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(@PathVariable Long workspaceId, @RequestBody WorkspaceRequest request) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(workspaceId, request));
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(@PathVariable Long workspaceId) {
        return ResponseEntity.ok(workspaceService.getWorkspace(workspaceId));
    }

    @GetMapping("")
    public ResponseEntity<Page<WorkspaceResponse>> getWorkspaceList(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(workspaceService.getWorkspaceList(page, size));
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<String> deleteWorkSpace(@PathVariable Long workspaceId) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseEntity.ok("성공적으로 삭제 되었습니다.");
    }

    @PostMapping("/invite")
    public ResponseEntity<String> inviteMemberToWorkspace(@RequestBody InviteWorkspaceRequest request) {
        workspaceService.inviteMemberToWorkspace(request);
        return ResponseEntity.ok("성공적으로 초대 되었습니다.");
    }
}
