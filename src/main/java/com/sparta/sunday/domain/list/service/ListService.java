package com.sparta.sunday.domain.list.service;

import com.sparta.sunday.config.RoleValidator;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
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

    @Transactional
    public ListResponse saveList(Long boardId, AuthUser authUser, ListRequest listRequest){

        roleValidator.validateRole(authUser);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드가 없습니다"));


        BoardList newBoardList = new BoardList(
                listRequest.getTitle(),
                listRequest.getOrder(),
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

        listRepository.delete(boardList);
    }

}
