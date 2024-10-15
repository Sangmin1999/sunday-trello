package com.sparta.sunday.domain.list.controller;

import com.sparta.sunday.domain.common.dto.AuthUser;
import com.sparta.sunday.domain.list.dto.request.ListRequest;
import com.sparta.sunday.domain.list.dto.response.ListResponse;
import com.sparta.sunday.domain.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/lists")
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<ListResponse> saveList(
            @PathVariable Long boardId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody ListRequest listRequest
    ) {
        return ResponseEntity.ok(listService.saveList(boardId, authUser, listRequest));
    }
}
