package com.sparta.sunday.domain.workspace.repository;

import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    @Query("""
            SELECT W
            FROM WorkspaceMember W
            JOIN FETCH W.member M
            WHERE M.id = :id AND W.id = :id1
            """)
    WorkspaceMember findByMemberIdAndWorkspaceId(Long id, Long id1);

}
