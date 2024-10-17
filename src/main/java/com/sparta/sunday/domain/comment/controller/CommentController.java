package com.sparta.sunday.domain.comment.controller;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.service.CommentService;
import com.sparta.sunday.domain.common.dto.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/cards")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{cardsId}/comments")
    public ResponseEntity<CommentResponse> saveComment(
            @PathVariable long cardsId,
            @RequestBody CommentRequest commentRequest,
            @AuthenticationPrincipal AuthUser authUser) throws SlackApiException, IOException {
        return ResponseEntity.ok(commentService.saveComment(cardsId, commentRequest, authUser));
    }

    @GetMapping("/{cardsId}/comments")
    public ResponseEntity<List<CommentResponse>> getComment(
            @PathVariable long cardsId) {
        return ResponseEntity.ok(commentService.getComment(cardsId));
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
