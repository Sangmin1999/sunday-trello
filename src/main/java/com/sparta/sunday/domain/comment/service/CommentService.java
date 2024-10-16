package com.sparta.sunday.domain.comment.service;

import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.entity.Comment;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public CommentResponse saveComment(long boardId, CommentRequest commentRequest, AuthUser authUser) {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 유저"));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 보드"));

        Comment comment = Comment.of(commentRequest, board, user);

        commentRepository.save(comment);

        return new CommentResponse(comment.getId(), comment.getContent());

    }

    public List<CommentResponse> getComment(long boardId) {

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 보드"));

        return commentRepository.findAllByDeletedAtNullAndBoard(board).stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getContent())).toList();

    }

    @Transactional
    public CommentResponse updateComment(CommentRequest commentRequest, Long commentId, AuthUser authUser) {

        Comment comment = findById(commentId, authUser);

        comment.update(commentRequest);

        return new CommentResponse(comment.getId(), comment.getContent());

    }

    @Transactional
    public String deleteComment(Long commentId, AuthUser authUser) {

        Comment comment = findById(commentId, authUser);

        comment.delete(LocalDateTime.now());

        return "댓글이 삭제되었습니다.";

    }

    public Comment findById(Long commentId, AuthUser authUser) {

        Comment comment = commentRepository.findByIdAndDeletedAtNull(commentId);

        if (authUser.getUserId() != comment.getUser().getId()) {
            throw new IllegalArgumentException("본인 댓글이 아닙니다.");
        }

        return comment;

    }

}
