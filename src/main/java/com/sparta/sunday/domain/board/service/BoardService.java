package com.sparta.sunday.domain.board.service;

import com.sparta.sunday.domain.board.dto.request.BoardRequest;
import com.sparta.sunday.domain.board.dto.response.BoardResponse;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;

    public void createBoard(Long workspaceId, BoardRequest request) {
        Board board = new Board();
        boardRepository.save(board);
    }

    public BoardResponse getBoard(Long boardId) {
    }

    public Page<BoardResponse> getBoardList(int page, int size) {
    }

    public BoardResponse updateBoard(Long boardId, BoardRequest request) {
    }

    public void deleteBoard(Long boardId) {
    }
}
