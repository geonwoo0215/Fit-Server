package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.domain.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Query("SELECT b FROM Board b " +
            "LEFT JOIN FETCH b.likes likes " +
            "WHERE likes.createdAt BETWEEN :startDate AND :endDate " +
            "ORDER BY SIZE(b.likes) DESC")
    Page<Board> findAllByLikeIncrease(Pageable pageable, @Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

}
