package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardCustomRepository {

    Page<Board> findAllByCondition(SearchBoardRequest searchBoardRequest, Pageable pageable);

}
