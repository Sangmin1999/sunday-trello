package com.sparta.sunday.domain.comment.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.entity.AlarmType;
import com.sparta.sunday.domain.alarm.service.AlarmService;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.entity.Comment;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final CardRepository cardRepository;
    private final AlarmService alarmService;
    private final AuthorizationValidator authorizationValidator;

    @Transactional
    public CommentResponse saveComment(long boardId, CommentRequest commentRequest, AuthUser authUser) throws SlackApiException, IOException {

        User user = userRepository.findById(authUser.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("없는 유저"));

        Card card = cardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("없는 카드"));

        authorizationValidator.checkWorkspaceAuthorization(
                user.getId(),
                card.getBoardList().getBoard().getWorkspace().getId(),
                WorkspaceRole.MEMBER);

        Comment comment = Comment.of(commentRequest, card, user);

        commentRepository.save(comment);

        alarmService.saveAlarm(
                AlarmType.COMMENT,
                comment.getId(),
                comment.getUser(),
                card.getActivities().get(0).getUser().getEmail());

        return new CommentResponse(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getContent());

    }

    @Transactional
    public CommentResponse updateComment(CommentRequest commentRequest, Long commentId, AuthUser authUser) {

        Comment comment = findById(commentId, authUser);

        comment.update(commentRequest);

        return new CommentResponse(
                comment.getId(),
                comment.getUser().getUsername(),
                comment.getContent());

    }

    @Transactional
    public String deleteComment(Long commentId, AuthUser authUser) {

        Comment comment = findById(commentId, authUser);

        comment.delete(LocalDateTime.now());

        return "댓글이 삭제되었습니다.";

    }

    public Comment findById(Long commentId, AuthUser authUser) {

        User user = User.fromAuthUser(authUser);

        Comment comment = commentRepository.findByIdAndDeletedAtNull(commentId);

        if (user.getId() != comment.getUser().getId()) {
            throw new IllegalArgumentException("본인 댓글이 아닙니다.");
        }

        return comment;

    }

}
