package com.sparta.sunday.domain.list.service;

import com.sparta.sunday.config.RoleValidator;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.card.repository.CardRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.list.dto.request.ListRequest;
import com.sparta.sunday.domain.list.dto.response.ListResponse;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import com.sparta.sunday.domain.user.enums.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class BoardListTest {
    @InjectMocks
    private ListService listService; // 실제 서비스

    @Mock
    private ListRepository listRepository;

    @Mock
    private BoardRepository boardRepository;

    @Mock
    private RoleValidator roleValidator;

    @Mock
    private CardRepository cardRepository;

    private Board mockBoard;
    private BoardList mockBoardList;
    private ListRequest mockListRequest;
    private AuthUser mockAuthUser;

    @BeforeEach
    public void setup() {
        mockBoard = new Board();
        ReflectionTestUtils.setField(mockBoard, "id", 1L);

        mockBoardList = new BoardList("List Title", 1, mockBoard);
        ReflectionTestUtils.setField(mockBoardList, "id", 1L);

        mockListRequest = new ListRequest();
        ReflectionTestUtils.setField(mockListRequest, "title", "List Title");
        ReflectionTestUtils.setField(mockListRequest, "order", 1);

        mockAuthUser = new AuthUser(1L, "email@example.com", UserRole.ROLE_ADMIN);
    }

    @Test
    public void 리스트_생성_성공() {
        // Given
        given(boardRepository.findById(anyLong())).willReturn(Optional.of(mockBoard));
        given(listRepository.findMaxOrderByBoardId(anyLong())).willReturn(Optional.of(1));  // 기존 리스트의 최대 order가 1이라고 설정

        BoardList newBoardList = new BoardList("List Title", 2, mockBoard);  // 새로운 리스트는 order가 2
        given(listRepository.save(any(BoardList.class))).willReturn(newBoardList);

        // When
        ListResponse response = listService.saveList(1L, mockAuthUser, mockListRequest);

        // Then
        assertThat(response.getOrder()).isEqualTo(2);  // 기대값 2, 실제로는 1로 반환됨

        verify(listRepository).save(any(BoardList.class));
    }

    @Test
    public void 리스트_생성_보드_없음() {
        // Given
        given(boardRepository.findById(anyLong())).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> listService.saveList(1L, mockAuthUser, mockListRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 보드가 없습니다");

        verify(listRepository, never()).save(any(BoardList.class));
    }

    @Test
    public void 리스트_업데이트_성공() {
        // Given
        Long listId = 1L;
        ListRequest updatedRequest = new ListRequest("Updated List", 2);
        given(listRepository.findById(listId)).willReturn(Optional.of(mockBoardList));

        // When
        ListResponse response = listService.updateList(listId, mockAuthUser, updatedRequest);

        // Then
        assertThat(response.getTitle()).isEqualTo("Updated List");
        assertThat(response.getOrder()).isEqualTo(2);

        verify(listRepository).findById(listId);
    }

    @Test
    public void 리스트_업데이트_리스트_없음() {
        // Given
        Long listId = 1L;
        given(listRepository.findById(listId)).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> listService.updateList(listId, mockAuthUser, mockListRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 리스트가 없습니다");

        verify(listRepository).findById(listId);
        verify(listRepository, never()).save(any(BoardList.class));
    }

    @Test
    public void 리스트_삭제_성공() {
        // Given
        Long listId = 1L;
        given(listRepository.findById(listId)).willReturn(Optional.of(mockBoardList));

        // When
        listService.deleteList(listId, mockAuthUser);

        // Then
        verify(listRepository).delete(mockBoardList);
        verify(cardRepository).deleteByBoardList(mockBoardList);
    }

    @Test
    public void 리스트_삭제_리스트_없음() {
        // Given
        Long listId = 1L;
        given(listRepository.findById(listId)).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> listService.deleteList(listId, mockAuthUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 리스트가 없습니다");

        verify(listRepository).findById(listId);
        verify(listRepository, never()).delete(any(BoardList.class));
    }

    @Test
    public void 리스트_순서_변경_성공() {
        // Given
        Long listId = 1L;
        int newOrder = 2;
        given(listRepository.findById(listId)).willReturn(Optional.of(mockBoardList));

        // When
        ListResponse response = listService.changeListOrder(listId, newOrder, mockAuthUser);

        // Then
        assertThat(response.getOrder()).isEqualTo(newOrder);
        verify(listRepository).updateListOrder(mockBoard.getId(), 1, newOrder);
    }

    @Test
    public void 리스트_순서_변경_리스트_없음() {
        // Given
        Long listId = 1L;
        int newOrder = 2;
        given(listRepository.findById(listId)).willReturn(Optional.empty());

        // When/Then
        assertThatThrownBy(() -> listService.changeListOrder(listId, newOrder, mockAuthUser))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("해당 리스트가 없습니다");

        verify(listRepository).findById(listId);
        verify(listRepository, never()).updateListOrder(anyLong(), anyInt(), anyInt());
    }

}
