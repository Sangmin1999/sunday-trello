package com.sparta.sunday.domain.board.service;

import com.sparta.sunday.domain.board.dto.request.BoardRequest;
import com.sparta.sunday.domain.board.dto.response.BoardResponse;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.repository.WorkspaceRepository;
import com.sparta.sunday.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    public void createBoard(Long workspaceId, BoardRequest request, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));
        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        if (request.getImgUrl() == null) {
            Board board = new Board(user, workspace, request.getTitle(), request.getBackgroundColor(), "color");
            boardRepository.save(board);
        } else if (request.getBackgroundColor() == null) {
            Board board = new Board(user, workspace, request.getTitle(), request.getImgUrl(), "image");
            boardRepository.save(board);
        }
    }

    public BoardResponse getBoard(Long boardId) {
        return new BoardResponse();
    }

    public Page<BoardResponse> getBoardList(int page, int size) {
        Pageable pageable = PageRequest.of(page,size, Sort.by("updatedAt").descending());
        Page<Board> boards = boardRepository.findAll(pageable);
        return boards.map(workspace -> new BoardResponse());
    }

    public BoardResponse updateBoard(Long boardId, BoardRequest request) {
        return new BoardResponse();
    }

    public void deleteBoard(Long boardId) {
    }
}
