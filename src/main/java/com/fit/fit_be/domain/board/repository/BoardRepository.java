package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.domain.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAllByOpenTrue(Pageable pageable);

}
