package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.domain.board.model.Board;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardCustomRepository {

    @Query(value = "SELECT b1_0.id, b1_0.content, b1_0.created_at, b1_0.created_date, b1_0.deleted, " +
            "b1_0.highest_temperature, b1_0.like_count, b1_0.lowest_temperature, b1_0.member_id, " +
            "b1_0.open, b1_0.place, b1_0.ranking, b1_0.road_condition, b1_0.updated_at, b1_0.version, " +
            "b1_0.weather " +
            "FROM board b1_0 " +
            "JOIN likes l1_0 ON b1_0.id = l1_0.board_id AND l1_0.created_date IN (:dates) " +
            "WHERE b1_0.created_date IN (:dates) AND b1_0.ranking = 1 " +
            "GROUP BY b1_0.id " +
            "ORDER BY COUNT(b1_0.id) DESC", nativeQuery = true)
    List<Board> findAllByLikeIncrease(Pageable pageable, @Param("dates") List<String> dates);

}
