package com.sparta.sunday.domain.workspace.entity;

import com.sparta.sunday.domain.common.entity.Timestamped;
import jakarta.persistence.*;

@Entity
public class WorkspaceMember extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // enum 으로 변경 (MEMBER, MANAGER)
    @Column(name = "role")
    private String role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private Workspace workspace;
}
