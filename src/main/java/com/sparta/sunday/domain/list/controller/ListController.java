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

    @PutMapping("/{listId}")
    public ResponseEntity<ListResponse> updateList(
            @PathVariable Long listId,
            @AuthenticationPrincipal AuthUser authUser,
            @RequestBody ListRequest listRequest
    ) {
        return ResponseEntity.ok(listService.updateList(listId, authUser, listRequest));
    }

    @DeleteMapping("/{listId}")
    public ResponseEntity<Void> deleteList(
            @PathVariable Long listId,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        listService.deleteList(listId, authUser);
        return ResponseEntity.noContent().build();
    }

    // 리스트 순서 변경 API
    @PatchMapping("/{listId}/order")
    public ResponseEntity<ListResponse> changeListOrder(
            @PathVariable Long listId,
            @RequestParam("newOrder") int newOrder,
            @AuthenticationPrincipal AuthUser authUser
    ) {
        return ResponseEntity.ok(listService.changeListOrder(listId, newOrder, authUser));
    }
}
