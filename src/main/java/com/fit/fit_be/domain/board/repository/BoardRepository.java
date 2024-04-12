package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.domain.board.model.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Query("SELECT b FROM Board b " +
            "INNER JOIN FETCH b.likes l " +
            "WHERE b.ranking = true " +
            "AND b.createdDate IN :dates " +
            "ORDER BY SIZE(b.likes) DESC")
    List<Board> findAllByLikeIncrease(Pageable pageable, @Param("dates") List<LocalDate> dates);
    
}
