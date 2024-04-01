package com.fit.fit_be.domain.cloth.repository;

import com.fit.fit_be.domain.cloth.model.Cloth;
import com.fit.fit_be.domain.cloth.model.ClothType;
import com.fit.fit_be.domain.cloth.model.QCloth;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.List;

@RequiredArgsConstructor
public class ClothRepositoryImpl implements ClothCustomRepository {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Page<Cloth> findAllByType(Pageable pageable, Long memberId, ClothType clothType) {

        List<Cloth> content = jpaQueryFactory
                .selectFrom(QCloth.cloth)
                .from(QCloth.cloth)
                .where(typeConditions(clothType))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = jpaQueryFactory
                .select(QCloth.cloth.count())
                .from(QCloth.cloth)
                .where(typeConditions(clothType));

        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
    }

    private BooleanExpression typeConditions(ClothType type) {
        if (type != null) {
            return QCloth.cloth.type.eq(type);
        }
        return null;
    }
}
