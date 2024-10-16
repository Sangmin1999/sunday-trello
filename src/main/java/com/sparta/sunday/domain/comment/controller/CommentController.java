package com.sparta.sunday.domain.comment.controller;

import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.service.CommentService;
import com.sparta.sunday.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{boardId}/comments")
    public ResponseEntity<CommentResponse> saveComment(
            @PathVariable long boardId,
            @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(commentService.saveComment(boardId, commentRequest, authUser));
    }

    @GetMapping("/{boardId}/comments")
    public ResponseEntity<List<CommentResponse>> getComment(
            @PathVariable long boardId) {
        return ResponseEntity.ok(commentService.getComment(boardId));
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @RequestBody CommentRequest commentRequest,
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(commentService.updateComment(commentRequest, commentId, authUser));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(
            @PathVariable Long commentId,
            @AuthenticationPrincipal AuthUser authUser) {
        return ResponseEntity.ok(commentService.deleteComment(commentId, authUser));
    }
}
