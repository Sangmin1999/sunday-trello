package com.sparta.sunday.domain.comment.service;

import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.entity.Comment;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponse saveComment(CommentRequest commentRequest) {

        Comment comment = Comment.of(commentRequest);

        commentRepository.save(comment);

        return new CommentResponse(comment.getId(), comment.getContent());
    }

    public List<CommentResponse> getComment() {

        return commentRepository.findAllByDeletedAtNull().stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getContent())).toList();

    }

    @Transactional
    public CommentResponse updateComment(CommentRequest commentRequest, Long commentId) {

        Comment comment = commentRepository.findByIdAndDeletedAtNull(commentId);

        comment.update(commentRequest);

        return new CommentResponse(comment.getId(), comment.getContent());
    }

    @Transactional
    public String deleteComment(Long commentId) {

        Comment comment = commentRepository.findByIdAndDeletedAtNull(commentId);

        comment.delete(LocalDateTime.now());

        return "댓글이 삭제되었습니다.";
    }
}
