package com.sparta.sunday.domain.card.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.slack.api.methods.SlackApiException;
import com.sparta.sunday.domain.attachment.dto.response.UploadAttachmentResponse;
import com.sparta.sunday.domain.attachment.service.AttachmentService;
import com.sparta.sunday.domain.card.dto.request.CardRequest;
import com.sparta.sunday.domain.card.dto.response.CardResponse;
import com.sparta.sunday.domain.card.entity.Card;
import com.sparta.sunday.domain.card.repository.CardActivityRepository;
import com.sparta.sunday.domain.card.repository.CardAttachmentRepository;
import com.sparta.sunday.domain.card.repository.CardManagerRepository;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.comment.repository.CommentRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.enums.UserRole;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;
    @Mock
    private CardManagerRepository cardManagerRepository;
    @Mock
    private ListRepository listRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CardActivityRepository cardActivityRepository;
    @Mock
    private CardAttachmentRepository cardAttachmentRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private AttachmentService attachmentService;
    @Mock
    private AuthorizationValidator authorizationValidator;
    @Mock
    private AmazonS3Client amazonS3Client;
    @Mock
    private CardActivityService cardActivityService;

    @InjectMocks
    private CardService cardService;

    private Card mockCard;
    private BoardList mockBoardList;
    private CardRequest mockCardRequest;
    private AuthUser mockAuthUser;

    @BeforeEach
    public void setup() {

        mockBoardList = new BoardList("Mock List", 1, null);
        ReflectionTestUtils.setField(mockBoardList, "id", 1L);

        mockCard = new Card("Mock Card", "Description", LocalDateTime.now(), mockBoardList);
        ReflectionTestUtils.setField(mockCard, "id", 1L);

        mockCardRequest = new CardRequest();
        ReflectionTestUtils.setField(mockCardRequest, "title", "Mock Card");
        ReflectionTestUtils.setField(mockCardRequest, "description", "Description");
        ReflectionTestUtils.setField(mockCardRequest, "dueTo", LocalDateTime.now().toString());
        ReflectionTestUtils.setField(mockCardRequest, "managerEmail", "manager@example.com");

        mockAuthUser = new AuthUser(1L, "user@example.com", UserRole.ROLE_ADMIN);
    }

    @Test
    public void 카드_생성_성공() throws SlackApiException, IOException {
        // Given
        given(listRepository.findById(anyLong())).willReturn(Optional.of(mockBoardList));
        given(cardRepository.save(any(Card.class))).willReturn(mockCard);

        User mockManager = new User(2L, "manager@example.com", UserRole.ROLE_ADMIN);
        given(userRepository.findByEmail("manager@example.com")).willReturn(Optional.of(mockManager));

        MockMultipartFile file = new MockMultipartFile("file", "test.jpg", "image/jpeg", "test".getBytes());

        // 첨부파일 업로드 시 반환값 설정
        UploadAttachmentResponse mockAttachmentResponse = new UploadAttachmentResponse(
                mockCard.getId(),
                file.getOriginalFilename(),
                mockAuthUser.getUserId(),
                new URL("http://test-url.com")
        );
        given(attachmentService.uploadAttachment(eq(file), eq(mockCard.getId()), anyLong(), eq(mockAuthUser)))
                .willReturn(ResponseEntity.ok(mockAttachmentResponse));

        doNothing().when(authorizationValidator).checkWorkspaceAuthorization(mockAuthUser.getUserId(), 1L, WorkspaceRole.MEMBER);

        // When
        CardResponse response = cardService.createCard(1L, 1L, mockCardRequest, file, mockAuthUser);

        // Then
        assertThat(response.getTitle()).isEqualTo("Mock Card");
        verify(cardRepository).save(any(Card.class));
        verify(attachmentService).uploadAttachment(eq(file), eq(mockCard.getId()), anyLong(), eq(mockAuthUser));
        verify(cardActivityService).logCardActivity(eq(mockCard), eq("카드 생성"), eq(mockAuthUser));
    }

    @Test
    public void 카드_생성_리스트_없음() {
        // Given
        given(listRepository.findById(anyLong())).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> cardService.createCard(1L, 1L, mockCardRequest, null, mockAuthUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 리스트가 없습니다");

        verify(cardRepository, never()).save(any(Card.class));
    }


}
