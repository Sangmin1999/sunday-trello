package com.sparta.sunday.domain.workspace.repository;

import com.sparta.sunday.domain.workspace.entity.Workspace;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkspaceRepository extends JpaRepository<Workspace, Long> {
}
