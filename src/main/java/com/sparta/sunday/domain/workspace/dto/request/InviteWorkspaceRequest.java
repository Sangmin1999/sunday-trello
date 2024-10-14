package com.sparta.sunday.domain.workspace.dto.request;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class InviteWorkspaceRequest {
    private List<String> inviteUserEmailList = new ArrayList<>();
}
