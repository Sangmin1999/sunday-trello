package com.sparta.sunday.domain.comment.controller;

import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/boards")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

//    @PostMapping("/{boardId}/comments")
    @PostMapping("/comments")
    public ResponseEntity<CommentResponse> saveComment(@RequestBody CommentRequest commentRequest) {
        return ResponseEntity.ok(commentService.saveComment(commentRequest));
    }

//    @GetMapping("/{boardId}/comments")
    @GetMapping("/comments")
    public ResponseEntity<List<CommentResponse>> getComment() {
        return ResponseEntity.ok(commentService.getComment());
    }

    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @RequestBody CommentRequest commentRequest,
            @PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.updateComment(commentRequest, commentId));
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.deleteComment(commentId));
    }
}
