package com.sparta.sunday.domain.board.controller;

import com.sparta.sunday.domain.board.dto.request.BoardRequest;
import com.sparta.sunday.domain.board.dto.response.BoardResponse;
import com.sparta.sunday.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardController {
    private final BoardService boardService;

    @PostMapping("/{workspaceId}")
    public ResponseEntity<String> createBoard(@PathVariable Long workspaceId, @RequestBody BoardRequest request) {
        boardService.createBoard(workspaceId, request);
        return ResponseEntity.ok("성공적으로 생성되었습니다.");
    }

    @GetMapping("/{boardId}")
    public ResponseEntity<BoardResponse> getBoard(@PathVariable Long boardId) {
        return ResponseEntity.ok(boardService.getBoard(boardId));
    }

    @GetMapping("")
    public ResponseEntity<Page<BoardResponse>> getBoardList(
            @RequestParam int page,
            @RequestParam int size
    ) {
        return ResponseEntity.ok(boardService.getBoardList(page, size));
    }

    @PatchMapping("/{boardId}")
    public ResponseEntity<BoardResponse> updateBoard(@PathVariable Long boardId, @RequestBody BoardRequest request) {
        return ResponseEntity.ok(boardService.updateBoard(boardId, request));
    }

    @DeleteMapping("/{boardId}")
    public ResponseEntity<String> deleteBoard(@PathVariable Long boardId) {
        boardService.deleteBoard(boardId);
        return ResponseEntity.ok("성공적으로 삭제되었습니다.");
    }
}
