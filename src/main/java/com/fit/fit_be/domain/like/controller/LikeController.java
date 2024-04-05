package com.fit.fit_be.domain.like.controller;

import com.fit.fit_be.domain.like.service.LikeService;
import com.fit.fit_be.domain.member.model.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class LikeController {

    private static final int MAX_RETRIES = 10;
    private final LikeService likeService;

    @PostMapping("/boards/{boardId}/like")
    public ResponseEntity<Void> save
            (
                    @PathVariable("boardId") Long boardId,
                    @AuthenticationPrincipal Member member
            ) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                likeService.save(boardId, member);
                return new ResponseEntity<>(HttpStatus.CREATED);
            } catch (ObjectOptimisticLockingFailureException e) {
                log.info("낙관적 락 실패. 재시도 중...");
            }
        }
        log.error("최대 재시도 횟수에 도달함. 작업 수행 불가.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @DeleteMapping("/boards/{boardId}/like")
    public ResponseEntity<Void> delete
            (
                    @PathVariable("boardId") Long boardId,
                    @AuthenticationPrincipal Member member
            ) {
        for (int i = 0; i < MAX_RETRIES; i++) {
            try {
                likeService.delete(boardId, member);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } catch (ObjectOptimisticLockingFailureException e) {
                log.info("낙관적 락 실패. 재시도 중...");
            }
        }
        log.error("최대 재시도 횟수에 도달함. 작업 수행 불가.");
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
