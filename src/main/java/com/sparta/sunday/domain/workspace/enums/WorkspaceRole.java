package com.sparta.sunday.domain.workspace.enums;

import com.sun.jdi.request.InvalidRequestStateException;

import java.util.Arrays;

public enum WorkspaceRole {
    READ_ONLY(1),
    MEMBER(2),
    MANAGER(3);

    private final int value;

    WorkspaceRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public static WorkspaceRole of(String role) {
        return Arrays.stream(WorkspaceRole.values())
                .filter(s -> s.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestStateException("유효하지 않은 OrderStatus"));
    }
}
