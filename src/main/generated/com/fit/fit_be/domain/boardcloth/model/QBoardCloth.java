package com.fit.fit_be.domain.boardcloth.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardCloth is a Querydsl query type for BoardCloth
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QBoardCloth extends EntityPathBase<BoardCloth> {

    private static final long serialVersionUID = 80321788L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardCloth boardCloth = new QBoardCloth("boardCloth");

    public final BooleanPath appropriate = createBoolean("appropriate");

    public final com.fit.fit_be.domain.board.model.QBoard board;

    public final com.fit.fit_be.domain.cloth.model.QCloth cloth;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public QBoardCloth(String variable) {
        this(BoardCloth.class, forVariable(variable), INITS);
    }

    public QBoardCloth(Path<? extends BoardCloth> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardCloth(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardCloth(PathMetadata metadata, PathInits inits) {
        this(BoardCloth.class, metadata, inits);
    }

    public QBoardCloth(Class<? extends BoardCloth> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new com.fit.fit_be.domain.board.model.QBoard(forProperty("board"), inits.get("board")) : null;
        this.cloth = inits.isInitialized("cloth") ? new com.fit.fit_be.domain.cloth.model.QCloth(forProperty("cloth"), inits.get("cloth")) : null;
    }

}

