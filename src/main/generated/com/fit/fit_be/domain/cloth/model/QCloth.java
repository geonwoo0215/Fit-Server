package com.fit.fit_be.domain.cloth.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QCloth is a Querydsl query type for Cloth
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QCloth extends EntityPathBase<Cloth> {

    private static final long serialVersionUID = 12539594L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QCloth cloth = new QCloth("cloth");

    public final com.fit.fit_be.global.common.base.QBaseEntity _super = new com.fit.fit_be.global.common.base.QBaseEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath information = createString("information");

    public final com.fit.fit_be.domain.member.model.QMember member;

    public final StringPath size = createString("size");

    public final EnumPath<ClothType> type = createEnum("type", ClothType.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QCloth(String variable) {
        this(Cloth.class, forVariable(variable), INITS);
    }

    public QCloth(Path<? extends Cloth> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QCloth(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QCloth(PathMetadata metadata, PathInits inits) {
        this(Cloth.class, metadata, inits);
    }

    public QCloth(Class<? extends Cloth> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.fit.fit_be.domain.member.model.QMember(forProperty("member")) : null;
    }

}

