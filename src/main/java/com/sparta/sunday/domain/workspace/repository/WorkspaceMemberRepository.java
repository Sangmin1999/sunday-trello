package com.sparta.sunday.domain.workspace.repository;

import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    WorkspaceMember findByMemberIdAndWorkspaceId(Long id, Long id1);
}
