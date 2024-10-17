package com.sparta.sunday.domain.workspace.repository;

import com.sparta.sunday.domain.workspace.entity.WorkspaceMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WorkspaceMemberRepository extends JpaRepository<WorkspaceMember, Long> {
    @Query("""
            SELECT distinct w
            FROM WorkspaceMember w
            JOIN FETCH w.member m
            JOIN FETCH w.workspace ws
            WHERE m.id = :userId AND ws.id = :workspaceId
            """)
    WorkspaceMember findByMemberIdAndWorkspaceId(Long userId, Long workspaceId);

    @Query("""
            SELECT distinct m.email
            FROM WorkspaceMember w
            JOIN w.member m
            JOIN w.workspace ws
            WHERE ws.id = :workspaceId
            """)
    List<String> findALlByWorkspaceId(Long workspaceId);

}
