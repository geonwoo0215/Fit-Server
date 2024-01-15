package com.fit.fit_be.domain.board.controller;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.board.service.BoardService;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping(value = "/boards", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody SaveBoardRequest saveBoardRequest,
                    @AuthenticationPrincipal Member member,
                    HttpServletRequest request
            ) {
        Long id = boardService.save(member, saveBoardRequest);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));
    }

    @GetMapping(value = "/boards/{boardId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<BoardResponse>> findById
            (
                    @PathVariable("boardId") Long boardId
            ) {
        BoardResponse response = boardService.findById(boardId);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @PatchMapping(value = "/boards/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update
            (
                    @PathVariable("boardId") Long boardId,
                    @RequestBody UpdateBoardRequest updateBoardRequest,
                    HttpServletRequest request
            ) {
        Long id = boardService.update(boardId, updateBoardRequest);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LOCATION, "/boards/" + id)
                .build();
    }

    @DeleteMapping(value = "/boards/{boardId}")
    public ResponseEntity<Void> delete
            (
                    @PathVariable("boardId") Long boardId
            ) {

        boardService.delete(boardId);

        return ResponseEntity
                .noContent()
                .build();
    }
}
