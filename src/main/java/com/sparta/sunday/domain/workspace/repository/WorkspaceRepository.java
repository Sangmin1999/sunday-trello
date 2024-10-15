package com.sparta.sunday.domain.workspace.repository;

import com.sparta.sunday.domain.workspace.entity.Workspace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {

    @Query("SELECT DISTINCT w FROM Workspace w " +
            "LEFT JOIN FETCH w.memberList m " +
            "WHERE w.owner.id = :userId OR m.member.id = :userId")
    Page<Workspace> findByOwnerOrMember(Long userId, Pageable pageable);
}
