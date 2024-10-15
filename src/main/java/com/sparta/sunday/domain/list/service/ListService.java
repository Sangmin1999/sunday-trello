package com.sparta.sunday.domain.list.service;

import com.sparta.sunday.config.JwtUtil;
import com.sparta.sunday.domain.board.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.list.dto.request.ListRequest;
import com.sparta.sunday.domain.list.dto.response.ListResponse;
import com.sparta.sunday.domain.list.entity.List;
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
    private final JwtUtil jwtUtil;

    @Transactional
    public ListResponse saveList(Long boardId, String token, ListRequest listRequest) {

        validateRole(token);

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 보드가 없습니다"));

        List newList = new List(
                listRequest.getTitle(),
                listRequest.getOrder(),
                board
        );
        List savedList = listRepository.save(newList);

        return new ListResponse(
                savedList.getId(),
                savedList.getTitle(),
                savedList.getOrder(),
                boardId
        );
    }

    private void validateRole(String token) {
        String role = jwtUtil.extractRole(token);
        if ("MANAGER".equals(role)) {
            throw new IllegalStateException("MANAGER 권한이 필요합니다.");
        }
    }
}
