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
        BooleanBuilder conditionsBuilder = new BooleanBuilder();

        conditionsBuilder.and(weatherCondition(searchBoardRequest));
        conditionsBuilder.and(roadConditionCondition(searchBoardRequest));
        conditionsBuilder.and(placeCondition(searchBoardRequest));
        conditionsBuilder.and(QBoard.board.open.isTrue());
        conditionsBuilder.and(lowestTemperatureConditions(searchBoardRequest));
        conditionsBuilder.and(highestTemperatureConditions(searchBoardRequest));

        List<Long> ids = jpaQueryFactory
                .select(QBoard.board.id)
                .from(QBoard.board)
                .where(conditionsBuilder)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Board> content = jpaQueryFactory
                .select(QBoard.board)
                .from(QBoard.board)
                .where(QBoard.board.id.in(ids))
                .fetch();


        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(QBoard.board.count())
                .from(QBoard.board)
                .where(conditionsBuilder);

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder lowestTemperatureConditions(SearchBoardRequest searchBoardRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchBoardRequest.getLowestTemperature() != null) {
            builder.and(QBoard.board.lowestTemperature.goe(searchBoardRequest.getLowestTemperature()));
        }

        return builder;
    }

    private BooleanBuilder highestTemperatureConditions(SearchBoardRequest searchBoardRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchBoardRequest.getHighestTemperature() != null) {
            builder.and(QBoard.board.highestTemperature.loe(searchBoardRequest.getHighestTemperature()));
        }

        return builder;
    }

    private BooleanBuilder weatherCondition(SearchBoardRequest searchBoardRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchBoardRequest.getWeather() != null) {
            builder.and(QBoard.board.weather.eq(searchBoardRequest.getWeather()));
        }

        return builder;
    }

    private BooleanBuilder roadConditionCondition(SearchBoardRequest searchBoardRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchBoardRequest.getRoadCondition() != null) {
            builder.and(QBoard.board.roadCondition.eq(searchBoardRequest.getRoadCondition()));
        }

        return builder;
    }

    private BooleanBuilder placeCondition(SearchBoardRequest searchBoardRequest) {
        BooleanBuilder builder = new BooleanBuilder();

        if (searchBoardRequest.getPlace() != null) {
            builder.and(QBoard.board.place.eq(searchBoardRequest.getPlace()));
        }

        return builder;
    }
}