package com.sparta.sunday.domain.list.controller;

import com.sparta.sunday.domain.list.dto.request.ListRequest;
import com.sparta.sunday.domain.list.dto.response.ListResponse;
import com.sparta.sunday.domain.list.service.ListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/{boardId}/lists")
public class ListController {

    private final ListService listService;

    @PostMapping
    public ResponseEntity<ListResponse> saveList(
            @PathVariable Long boardId,
            @RequestHeader(value = "Authorization", required = false) String token,
            @RequestBody ListRequest listRequest
    ) {
        return ResponseEntity.ok(listService.saveList(boardId, token, listRequest));
    }
}
