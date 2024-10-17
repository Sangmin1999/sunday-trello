package com.sparta.sunday.domain.workspace.entity;

import com.sparta.sunday.domain.common.entity.Timestamped;
import com.sparta.sunday.domain.user.entity.User;
import com.sparta.sunday.domain.workspace.enums.WorkspaceRole;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Entity
@Getter
@RequiredArgsConstructor
public class WorkspaceMember extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private WorkspaceRole role;   // MEMBER, MANAGER

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User member;

    public WorkspaceMember(
            WorkspaceRole workspaceRole,
            Workspace workspace,
            User user
    ) {
        this.role = workspaceRole;
        this.workspace = workspace;
        this.member = user;
    }

    public void changeRole(WorkspaceRole newRole) {
        this.role = newRole;
    }
}
