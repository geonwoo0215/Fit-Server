package com.fit.fit_be.domain.comment.controller;

import com.fit.fit_be.domain.comment.dto.request.CommentSaveRequest;
import com.fit.fit_be.domain.comment.dto.response.CommentResponse;
import com.fit.fit_be.domain.comment.service.CommentService;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping(value = "/boards/{boardId}/comments", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody CommentSaveRequest commentSaveRequest,
                    @PathVariable("boardId") Long boardId,
                    @AuthenticationPrincipal Member member,
                    HttpServletRequest request
            ) {
        Long id = commentService.save(member, boardId, commentSaveRequest);
        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));
    }

    @GetMapping(value = "/boards/{boardId}/comments", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<CommentResponse>>> findAllByBoardId
            (
                    @PathVariable("boardId") Long boardId,
                    @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable
            ) {
        Page<CommentResponse> commentDTOList = commentService.findAllByBoardId(boardId, pageable);
        return ResponseEntity.ok(new ApiResponse<>(commentDTOList));
    }
}
