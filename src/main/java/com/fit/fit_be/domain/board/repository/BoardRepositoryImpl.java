package com.fit.fit_be.domain.board.repository;

import com.fit.fit_be.domain.board.dto.request.SearchBoardRequest;
import com.fit.fit_be.domain.board.model.Board;
import com.fit.fit_be.domain.board.model.QBoard;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class BoardRepositoryImpl implements BoardCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Board> findAllByCondition(SearchBoardRequest searchBoardRequest, Pageable pageable) {
        List<Board> content = jpaQueryFactory
                .selectFrom(QBoard.board)
                .from(QBoard.board)
                .where(
                        temperatureConditions(searchBoardRequest)
                                .and(QBoard.board.open.isTrue())
                )
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(QBoard.board.count())
                .from(QBoard.board)
                .where(
                        temperatureConditions(searchBoardRequest)
                );

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder temperatureConditions(SearchBoardRequest searchBoardRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchBoardRequest.getLowestTemperature() != null) {
            builder.and(QBoard.board.lowestTemperature.goe(searchBoardRequest.getLowestTemperature()));
        }

        if (searchBoardRequest.getHighestTemperature() != null) {
            builder.and(QBoard.board.highestTemperature.loe(searchBoardRequest.getHighestTemperature()));
        }

        return builder;
    }

}
