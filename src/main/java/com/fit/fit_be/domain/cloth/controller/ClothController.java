package com.fit.fit_be.domain.cloth.controller;

import com.fit.fit_be.domain.cloth.dto.request.SaveClothRequest;
import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
import com.fit.fit_be.domain.cloth.dto.response.ClothResponse;
import com.fit.fit_be.domain.cloth.service.ClothService;
import com.fit.fit_be.domain.member.model.Member;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ClothController {

    private final ClothService clothService;

    @PostMapping(value = "/cloths", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody SaveClothRequest saveClothRequest,
                    @AuthenticationPrincipal Member member,
                    HttpServletRequest request
            ) {
        log.info("aaa=={}", saveClothRequest.getType());
        log.info("aaa=={}", saveClothRequest.getSize());
        Long id = clothService.save(member, saveClothRequest);

        return ResponseEntity
                .created(URI.create(request.getRequestURI() + "/" + id))
                .body(new ApiResponse<>(id));
    }

    @GetMapping(value = "/cloths/{clothId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<ClothResponse>> findById
            (
                    @PathVariable("clothId") Long clothId
            ) {
        ClothResponse response = clothService.findById(clothId);

        return ResponseEntity.ok(new ApiResponse<>(response));
    }

    @GetMapping(value = "/cloths", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<List<ClothResponse>>> findAll
            (
                    @RequestParam("type") String type,
                    @PageableDefault(sort = "updatedAt", direction = Sort.Direction.DESC) Pageable pageable,
                    @AuthenticationPrincipal Member member

            ) {
        List<ClothResponse> list = clothService.findAll(type, member.getId(), pageable);

        return ResponseEntity.ok(new ApiResponse<>(list));
    }

    @PatchMapping(value = "/cloths/{clothId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> update
            (
                    @PathVariable("clothId") Long boardId,
                    @RequestBody UpdateClothRequest updateClothRequest
            ) {
        Long id = clothService.update(boardId, updateClothRequest);

        return ResponseEntity
                .noContent()
                .header(HttpHeaders.LOCATION, "/cloths/" + id)
                .build();
    }

    @DeleteMapping(value = "/cloths/{clothId}")
    public ResponseEntity<Void> delete
            (
                    @PathVariable("clothId") Long boardId
            ) {

        clothService.delete(boardId);

        return ResponseEntity
                .noContent()
                .build();
    }

}
