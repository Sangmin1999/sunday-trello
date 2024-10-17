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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;
    private final RoleValidator roleValidator;
    private final CardRepository cardRepository;

    @Transactional
    public ListResponse saveList(Long boardId, AuthUser authUser, ListRequest listRequest) {

        roleValidator.validateRole(authUser);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드가 없습니다"));

        // 같은 보드 내에서 마지막 리스트의 order 값을 조회
        int maxOrder = listRepository.findMaxOrderByBoardId(boardId).orElse(0);

        // 새 리스트는 maxOrder + 1로 설정
        BoardList newBoardList = new BoardList(
                listRequest.getTitle(),
                maxOrder + 1, // 자동으로 순서를 부여
                board
        );

        BoardList savedBoardList = listRepository.save(newBoardList);
        return new ListResponse(savedBoardList);

    }

    @Transactional
    public ListResponse updateList(Long listId, AuthUser authUser, ListRequest listRequest) {

        roleValidator.validateRole(authUser);

        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        boardList.update(listRequest.getTitle(), listRequest.getOrder());
        return new ListResponse(boardList);
    }

    @Transactional
    public void deleteList(Long listId, AuthUser authUser) {

        roleValidator.validateRole(authUser);

        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        // 1. 해당 리스트의 모든 카드 삭제
        cardRepository.deleteByBoardList(boardList);

        listRepository.delete(boardList);

        // 리스트 삭제 후 그 뒤의 리스트들의 순서를 하나씩 감소
        listRepository.decrementOrdersAfterDelete(boardList.getBoard().getId(), boardList.getOrder());
    }

    @Transactional
    public ListResponse changeListOrder(Long listId, int newOrder, AuthUser authUser) {
        roleValidator.validateRole(authUser);

        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        // 현재 리스트의 순서
        int oldOrder = boardList.getOrder();

        // 순서 변경 쿼리 호출
        listRepository.updateListOrder(boardList.getBoard().getId(), oldOrder, newOrder);

        // 리스트의 순서를 업데이트
        boardList.update(boardList.getTitle(), newOrder);

        return new ListResponse(boardList);
    }

}
