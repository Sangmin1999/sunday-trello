package com.sparta.sunday.domain.workspace.dto.response;

import lombok.Getter;

@Getter
public class WorkspaceResponse {
    private Long id;
    private String name;
    private String description;

    public WorkspaceResponse(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }
}
