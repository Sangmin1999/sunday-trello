package com.sparta.sunday.domain.board.service;

import com.sparta.sunday.domain.board.dto.request.BoardRequest;
import com.sparta.sunday.domain.board.dto.response.BoardResponse;
import com.sparta.sunday.domain.board.entity.Board;
import com.sparta.sunday.domain.board.repository.BoardRepository;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.user.repository.UserRepository;
import com.sparta.sunday.domain.workspace.entity.Workspace;
import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import com.sparta.sunday.domain.workspace.repository.WorkspaceMemberRepository;
import com.sparta.sunday.domain.workspace.repository.WorkspaceRepository;
import com.sparta.sunday.domain.common.exception.EntityNotFoundException;
import com.sparta.sunday.domain.common.exception.UnAuthorizedException;
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
    private final WorkspaceMemberRepository workspaceMemberRepository;

    @Transactional
    public void createBoard(Long workspaceId, BoardRequest request, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        checkWorkspaceAuthorization(user, workspace);

        if (request.getImgUrl() == null) {
            Board board = new Board(
                    user,
                    workspace,
                    request.getTitle(),
                    request.getContent(),
                    request.getBackgroundColor(),
                    "color");
            boardRepository.save(board);
        } else if (request.getBackgroundColor() == null) {
            Board board = new Board(
                    user,
                    workspace,
                    request.getTitle(),
                    request.getContent(),
                    request.getImgUrl(),
                    "image");
            boardRepository.save(board);
        }
    }

    public BoardResponse getBoard(Long workspaceId, Long boardId, Long userId) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        checkWorkspaceMember(user, workspace);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 보드입니다."));

        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImgUrl(),
                board.getBackgroundColor()
        );
    }

    public Page<BoardResponse> getBoardList(int page, int size, Long workspaceId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        checkWorkspaceMember(user, workspace);

        Pageable pageable = PageRequest.of(page,size, Sort.by("updatedAt").descending());
        Page<Board> boards = boardRepository.findByWorkspaceId(workspaceId, pageable);

        return boards.map(board -> new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImgUrl(),
                board.getBackgroundColor()
        ));
    }

    @Transactional
    public BoardResponse updateBoard(
            Long boardId,
            Long workspaceId,
            Long userId,
            BoardRequest request
    ) {

        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        checkWorkspaceAuthorization(user, workspace);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 보드입니다."));

        board.update(
                request.getTitle(),
                request.getContent(),
                request.getImgUrl(),
                request.getBackgroundColor()
        );

        return new BoardResponse(
                board.getId(),
                board.getTitle(),
                board.getContent(),
                board.getImgUrl(),
                board.getBackgroundColor()
        );
    }

    @Transactional
    public void deleteBoard(Long boardId, Long workspaceId, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 유저입니다."));

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 워크스페이스입니다."));

        checkWorkspaceAuthorization(user, workspace);

        Board board = boardRepository.findById(boardId).orElseThrow(() -> new EntityNotFoundException("존재하지 않는 보드입니다."));

        boardRepository.delete(board);
    }

    private void checkWorkspaceAuthorization(User user, Workspace workspace) {

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), workspace.getId());

        if(!workspaceMember.getRole().equals(WorkspaceRole.MANAGER)) {
            throw new UnAuthorizedException("해당 기능에 대한 권한이 없습니다.");
        }
    }

    private void checkWorkspaceMember(User user, Workspace workspace) {

        WorkspaceMember workspaceMember = workspaceMemberRepository.findByMemberIdAndWorkspaceId(user.getId(), workspace.getId());

        if(workspaceMember == null) {
            throw new UnAuthorizedException("해당 워크스페이스에 속한 멤버가 아닙니다.");
        }
    }
}
