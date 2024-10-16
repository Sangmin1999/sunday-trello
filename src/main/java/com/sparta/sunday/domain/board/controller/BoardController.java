package com.sparta.sunday.domain.board.controller;

import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.board.dto.request.BoardRequest;
import com.sparta.sunday.domain.board.dto.response.BoardResponse;
import com.sparta.sunday.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/{workspaceId}")
    public ResponseEntity<String> createBoard(
            @PathVariable Long workspaceId,
            @RequestBody BoardRequest request,
            @AuthenticationPrincipal AuthUser authUser
            ) {
        boardService.createBoard(workspaceId, request, authUser.getUserId());
        return ResponseEntity.ok("성공적으로 생성되었습니다.");
    }

    @GetMapping("/{boardId}/workspace/{workspaceId}")
    public ResponseEntity<BoardResponse> getBoard(
            @PathVariable Long workspaceId,
            @PathVariable Long boardId,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(boardService.getBoard(workspaceId, boardId, authUser.getUserId()));
    }

    @GetMapping("/workspace/{workspaceId}")
    public ResponseEntity<Page<BoardResponse>> getBoardList(
            @RequestParam int page,
            @RequestParam int size,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(boardService.getBoardList(page, size, workspaceId, authUser.getUserId()));
    }

    @PatchMapping("/{boardId}/workspace/{workspaceId}")
    public ResponseEntity<BoardResponse> updateBoard(
            @PathVariable Long boardId,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody BoardRequest request
    ) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, workspaceId, authUser.getUserId(), request));
    }

    @DeleteMapping("/{boardId}/workspace/{workspaceId}")
    public ResponseEntity<String> deleteBoard(
            @PathVariable Long boardId,
            @PathVariable Long workspaceId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        boardService.deleteBoard(boardId, workspaceId, authUser.getUserId());
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }
}
