package com.sparta.sunday.domain.list.service;

import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.common.exception.ReadOnlyRoleException;
import com.sparta.sunday.domain.list.dto.request.ListRequest;
import com.sparta.sunday.domain.list.dto.response.ListResponse;
import com.sparta.sunday.domain.list.entity.BoardList;
import com.sparta.sunday.domain.list.repository.ListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ListService {

    private final ListRepository listRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public ListResponse saveList(Long boardId, AuthUser authUser, ListRequest listRequest){

        validateRole(authUser);

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

        validateRole(authUser);
        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        boardList.update(listRequest.getTitle(), listRequest.getOrder());
        return new ListResponse(boardList);
    }

    @Transactional
    public void deleteList(Long listId, AuthUser authUser) {

        validateRole(authUser);
        BoardList boardList = listRepository.findById(listId)
                .orElseThrow(() -> new IllegalArgumentException("해당 리스트가 없습니다"));

        listRepository.delete(boardList);
    }

    private void validateRole(AuthUser authUser) {
        if (authUser.getAuthorities().contains(new SimpleGrantedAuthority("MEMBER"))) {
            throw new ReadOnlyRoleException("읽기 전용 멤버는 작업을 수행할 수 없습니다.");
        }
    }


}
