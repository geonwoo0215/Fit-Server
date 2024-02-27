package com.fit.fit_be.domain.board.controller;

import com.fit.fit_be.domain.board.dto.request.SaveBoardRequest;
import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.dto.request.UpdateBoardRequest;
import com.fit.fit_be.domain.board.dto.response.BoardResponse;
import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.domain.board.service.BoardService;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@Slf4j
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
                    @PathVariable("boardId") Long boardId,
                    @AuthenticationPrincipal Member member
            ) {
        BoardResponse response = boardService.findById(member.getId(), boardId);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping(value = "/boards", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> findAll(
            @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @RequestParam(value = "lowestTemperature", required = false) Long lowestTemperature,
            @RequestParam(value = "highestTemperature", required = false) Long highestTemperature,
            @RequestParam(value = "weather", required = false) String weather,
            @RequestParam(value = "roadCondition", required = false) String roadCondition,
            @RequestParam(value = "place", required = false) String place,
            @RequestParam(value = "mine", required = false) Boolean mine,
            @AuthenticationPrincipal Member member
    ) {

        SearchBoardRequest.SearchBoardRequestBuilder searchBoardRequestBuilder = SearchBoardRequest.searchBoardRequestBuilder(lowestTemperature, highestTemperature);

        log.info("asdfas = {}", weather);

        if (weather != null) {
            searchBoardRequestBuilder.weather(Weather.of(weather));
        }

        if (place != null) {
            searchBoardRequestBuilder.place(Place.of(place));
        }

        if (roadCondition != null) {
            searchBoardRequestBuilder.roadCondition(RoadCondition.of(roadCondition));
        }

        if (mine != null) {
            searchBoardRequestBuilder.mine(mine);
        }

        SearchBoardRequest searchBoardRequest = searchBoardRequestBuilder.build();

        Page<BoardResponse> responsePage = boardService.findAllByCondition(searchBoardRequest, pageable, member.getId());
        return ResponseEntity.ok(new ApiResponse<>(responsePage));
    }

    @GetMapping(value = "/boards/weekly-ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> findAllByWeeklyLikeCount(
            @AuthenticationPrincipal Member member,
            @PageableDefault Pageable pageable
    ) {
        Page<BoardResponse> responsePage = boardService.findAllByWeeklyLikeCount(pageable, member.getId());
        return ResponseEntity.ok(new ApiResponse<>(responsePage));
    }

    @GetMapping(value = "/boards/daily-ranking", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Page<BoardResponse>>> findAllByDailyLikeCount(
            @AuthenticationPrincipal Member member,
            @PageableDefault Pageable pageable
    ) {
        Page<BoardResponse> responsePage = boardService.findAllByDailyLikeCount(pageable, member.getId());
        return ResponseEntity.ok(new ApiResponse<>(responsePage));
    }

    @PatchMapping(value = "/boards/{boardId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update
            (
                    @PathVariable("boardId") Long boardId,
                    @RequestBody UpdateBoardRequest updateBoardRequest
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
