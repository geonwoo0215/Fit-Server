package com.fit.fit_be.domain.board.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoard is a Querydsl query type for Board
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoard extends EntityPathBase<Board> {

    private static final long serialVersionUID = -319379870L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoard board = new QBoard("board");

    public final com.fit.fit_be.global.common.base.QBaseEntity _super = new com.fit.fit_be.global.common.base.QBaseEntity(this);

    public final ListPath<com.fit.fit_be.domain.boardcloth.model.BoardCloth, com.fit.fit_be.domain.boardcloth.model.QBoardCloth> boardCloths = this.<com.fit.fit_be.domain.boardcloth.model.BoardCloth, com.fit.fit_be.domain.boardcloth.model.QBoardCloth>createList("boardCloths", com.fit.fit_be.domain.boardcloth.model.BoardCloth.class, com.fit.fit_be.domain.boardcloth.model.QBoardCloth.class, PathInits.DIRECT2);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> highestTemperature = createNumber("highestTemperature", Long.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<com.fit.fit_be.domain.image.model.Image, com.fit.fit_be.domain.image.model.QImage> images = this.<com.fit.fit_be.domain.image.model.Image, com.fit.fit_be.domain.image.model.QImage>createList("images", com.fit.fit_be.domain.image.model.Image.class, com.fit.fit_be.domain.image.model.QImage.class, PathInits.DIRECT2);

    public final NumberPath<Integer> likeCount = createNumber("likeCount", Integer.class);

    public final NumberPath<Long> lowestTemperature = createNumber("lowestTemperature", Long.class);

    public final com.fit.fit_be.domain.member.model.QMember member;

    public final BooleanPath open = createBoolean("open");

    public final EnumPath<Place> place = createEnum("place", Place.class);

    public final EnumPath<RoadCondition> roadCondition = createEnum("roadCondition", RoadCondition.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public final NumberPath<Long> version = createNumber("version", Long.class);

    public final EnumPath<Weather> weather = createEnum("weather", Weather.class);

    public QBoard(String variable) {
        this(Board.class, forVariable(variable), INITS);
    }

    public QBoard(Path<? extends Board> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoard(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoard(PathMetadata metadata, PathInits inits) {
        this(Board.class, metadata, inits);
    }

    public QBoard(Class<? extends Board> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.fit.fit_be.domain.member.model.QMember(forProperty("member")) : null;
    }

}

