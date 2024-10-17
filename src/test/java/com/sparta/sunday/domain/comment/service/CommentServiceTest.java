package com.sparta.sunday.domain.comment.service;

import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.alarm.service.AlarmService;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.entity.CardActivity;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.comment.dto.CommentRequest;
import com.sparta.sunday.domain.comment.dto.CommentResponse;
import com.sparta.sunday.domain.comment.entity.Comment;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardRepository cardRepository;
    @Mock
    private AlarmService alarmService;
    @Mock
    private AuthorizationValidator authorizationValidator;
    @InjectMocks
    private CommentService commentService;

    private User testUser;
    private AuthUser testAuthUser;
    private Comment testComment;
    private Card testCard;
    private BoardList testBoardList;
    private CommentRequest testCommentRequest;
    private CommentResponse testCommentResponse;
    private Board testBoard;
    private Workspace testWorkspace;
    private List<CardActivity> testActivities = new ArrayList<>();
    private CardActivity testCardActivity;


    @BeforeEach
    public void setup() {

        testUser = new User("닉네임", "1234", "test@mail.com", UserRole.ROLE_ADMIN);

        testAuthUser = new AuthUser(1L, "test@mail.com", UserRole.ROLE_ADMIN);

        testCommentRequest = new CommentRequest();
        testCommentRequest.setContent("내용");

        testCommentResponse = new CommentResponse(1L, testUser.getUsername(), testCommentRequest.getContent());

        testWorkspace = new Workspace();
        ReflectionTestUtils.setField(testWorkspace, "id", 1L);

        testBoard = new Board();
        ReflectionTestUtils.setField(testBoard, "workspace", testWorkspace);

        testBoardList = new BoardList("Mock List", 1, null);
        ReflectionTestUtils.setField(testBoardList, "id", 1L);
        ReflectionTestUtils.setField(testBoardList, "board", testBoard);

        testCardActivity = new CardActivity();
        ReflectionTestUtils.setField(testCardActivity, "user", testUser);
        testActivities.add(testCardActivity);

        testCard = new Card("Mock Card", "Description", LocalDateTime.now(), testBoardList);
        ReflectionTestUtils.setField(testCard, "id", 1L);
        ReflectionTestUtils.setField(testCard, "boardList", testBoardList);
        ReflectionTestUtils.setField(testCard, "activities", testActivities);

        testComment = Comment.of(testCommentRequest, testCard, User.fromAuthUser(testAuthUser));

    }

    @Test
    @DisplayName("댓글 생성 DB에 저장 성공")
    void saveComment() throws SlackApiException, IOException {

        // given
        given(userRepository.findById(anyLong())).willReturn(Optional.ofNullable(testUser));
        given(cardRepository.findById(anyLong())).willReturn(Optional.ofNullable(testCard));

        // when
        CommentResponse commentResponse = commentService.saveComment(1L, testCommentRequest, testAuthUser);

        // then
        assertEquals(testCommentResponse.getContent(), commentResponse.getContent());
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment() {

        // given
        String updateContent = "변경됨";

        CommentRequest commentRequest = new CommentRequest();
        commentRequest.setContent(updateContent);

        given(commentRepository.findByIdAndDeletedAtNull(anyLong())).willReturn(testComment);

        // when
        CommentResponse commentResponse = commentService.updateComment(commentRequest, 1L, testAuthUser);

        // then
        assertEquals(updateContent, commentResponse.getContent());
    }

    @Test
    void deleteComment() {

        // given
        given(commentRepository.findByIdAndDeletedAtNull(anyLong())).willReturn(testComment);

        // when
        String deleteComment = commentService.deleteComment(1L, testAuthUser);

        // then
        assertEquals("댓글이 삭제되었습니다.", deleteComment);
        assertNotNull(testComment.getDeletedAt());
    }

    @Test
    void findById() {

        // given
        AuthUser authUser = new AuthUser(0L, "", UserRole.ROLE_ADMIN);
        given(commentRepository.findByIdAndDeletedAtNull(anyLong())).willReturn(testComment);

        // when
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> commentService.findById(1L, authUser));

        // then
        assertEquals("본인 댓글이 아닙니다.", exception.getMessage());

    }
}