package com.sparta.sunday.domain.workspace.controller;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.workspace.dto.request.ChangeWorkspaceMemeberRoleRequest;
import com.sparta.sunday.domain.workspace.dto.request.InviteWorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.sunday.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.sunday.domain.workspace.service.WorkspaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    public ResponseEntity<String> createWorkspace(@RequestBody WorkspaceRequest request, @AuthenticationPrincipal AuthUser authUser) {
        workspaceService.createWorkspace(request, authUser.getUserId());
        return ResponseEntity.ok("워크스페이스가 성공적으로 생성되었습니다.");
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            @PathVariable Long workspaceId,
            @RequestBody WorkspaceRequest request,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(workspaceService.updateWorkspace(
                workspaceId,
                request,
                authUser.getUserId()
        ));
    }

    @GetMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> getWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(workspaceService.getWorkspace(workspaceId, authUser.getUserId()));
    }

    @GetMapping("")
    public ResponseEntity<Page<WorkspaceResponse>> getWorkspaceList(
            @AuthenticationPrincipal AuthUser authUser,
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(workspaceService.getWorkspaceList(authUser.getUserId(), page, size));
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<String> deleteWorkSpace(@PathVariable Long workspaceId, @AuthenticationPrincipal AuthUser authUser) {
        workspaceService.deleteWorkspace(workspaceId, authUser.getUserId());
        return ResponseEntity.ok("성공적으로 삭제 되었습니다.");
    }

    @PostMapping("/invite/{workspaceId}")
    public ResponseEntity<String> inviteMemberToWorkspace(
            @RequestBody InviteWorkspaceRequest request,
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long workspaceId
    ) throws SlackApiException, IOException {
        workspaceService.inviteMemberToWorkspace(request, authUser.getUserId(), workspaceId);
        return ResponseEntity.ok("성공적으로 초대 되었습니다.");
    }
}
