package com.sparta.sunday.domain.board.service;

import com.sparta.sunday.domain.board.dto.request.BoardRequest;
import com.sparta.sunday.domain.board.dto.response.BoardResponse;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.common.exception.EntityNotFoundException;
import com.sparta.sunday.domain.common.validator.AuthorizationValidator;
import com.sparta.sunday.domain.common.validator.ImgUrlValidator;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.service.AuthService;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.service.WorkspaceService;
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
    private final AuthorizationValidator authorizationValidator;
    private final ImgUrlValidator imgUrlValidator;
    private final AuthService authService;
    private final WorkspaceService workspaceService;

    @Transactional
    public void createBoard(Long workspaceId, BoardRequest request, Long userId) {

        User user = authService.findUser(userId);

        Workspace workspace = workspaceService.findWorkspace(workspaceId);

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MEMBER);

        if (request.getImgUrl() != null && request.getBackgroundColor() != null) {
            throw new IllegalArgumentException("배경색과 이미지 둘 중 하나만 입력해야 합니다.");
        }

        String background = request.getImgUrl() == null
                ? request.getBackgroundColor() : imgUrlValidator.isValidImageUrl(request.getImgUrl())
                ? request.getImgUrl() : null;

        String backgroundType = request.getImgUrl() == null ? "color" : "image";

        Board board = new Board(
                user,
                workspace,
                request.getTitle(),
                request.getContent(),
                background,
                backgroundType
        );
        boardRepository.save(board);
    }

    public BoardResponse getBoard(Long workspaceId, Long boardId, Long userId) {

        authorizationValidator.checkWorkspaceMember(userId, workspaceId);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 보드입니다."));

        return new BoardResponse(board);
    }

    public Page<BoardResponse> getBoardList(int page, int size, Long workspaceId, Long userId) {

        authorizationValidator.checkWorkspaceMember(userId, workspaceId);

        Pageable pageable = PageRequest.of(page - 1,size, Sort.by("modifiedAt").descending());
        Page<Board> boards = boardRepository.findByWorkspaceId(workspaceId, pageable);

        return boards.map(BoardResponse::new);
    }

    @Transactional
    public BoardResponse updateBoard(
            Long boardId,
            Long workspaceId,
            Long userId,
            BoardRequest request
    ) {

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MEMBER);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 보드입니다."));

        String background = request.getImgUrl() == null
                ? request.getBackgroundColor() : imgUrlValidator.isValidImageUrl(request.getImgUrl())
                ? request.getImgUrl() : null;

        String backgroundType = request.getImgUrl() == null ? "color" : "image";

        board.update(
                request.getTitle(),
                request.getContent(),
                background,
                backgroundType
        );

        return new BoardResponse(board);
    }

    @Transactional
    public void deleteBoard(Long boardId, Long workspaceId, Long userId) {

        authorizationValidator.checkWorkspaceAuthorization(userId, workspaceId, WorkspaceRole.MEMBER);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 보드입니다."));

        boardRepository.delete(board);
    }
}
