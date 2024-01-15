package com.fit.fit_be.domain.cloth.controller;

import com.fit.fit_be.domain.cloth.dto.request.SaveClothRequest;
import com.fit.fit_be.domain.cloth.dto.request.UpdateClothRequest;
import com.fit.fit_be.domain.cloth.dto.response.ClothResponse;
import com.fit.fit_be.domain.cloth.service.ClothService;
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
public class ClothController {

    private final ClothService clothService;

    @PostMapping(value = "/cloths", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse<Long>> save
            (
                    @RequestBody SaveClothRequest saveClothRequest,
                    @AuthenticationPrincipal Member member,
                    HttpServletRequest request
            ) {
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
