package com.sparta.sunday.domain.workspace.entity;

import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRoleEnum;
import jakarta.persistence.*;

@Entity
public class WorkspaceMember extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    private WorkspaceRoleEnum role;   // MEMBER, MANAGER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;
}
