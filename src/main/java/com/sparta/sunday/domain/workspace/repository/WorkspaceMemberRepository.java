package com.sparta.sunday.domain.workspace.repository;

import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    WorkspaceMember findByMemberIdAndWorkspaceId(Long id, Long id1);

    Optional<WorkspaceMember> findByMemberIdAndRole(Long userId, WorkspaceRole role);
}
