package com.fit.fit_be.domain.weather.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeatherArea is a Querydsl query type for WeatherArea
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWeatherArea extends EntityPathBase<WeatherArea> {

    private static final long serialVersionUID = -1355098005L;

    public static final QWeatherArea weatherArea = new QWeatherArea("weatherArea");

    public final NumberPath<Long> areaCode = createNumber("areaCode", Long.class);

    public final NumberPath<Integer> gridX = createNumber("gridX", Integer.class);

    public final NumberPath<Integer> gridY = createNumber("gridY", Integer.class);

    public final StringPath step1 = createString("step1");

    public final StringPath step2 = createString("step2");

    public final StringPath step3 = createString("step3");

    public QWeatherArea(String variable) {
        super(WeatherArea.class, forVariable(variable));
    }

    public QWeatherArea(Path<? extends WeatherArea> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeatherArea(PathMetadata metadata) {
        super(WeatherArea.class, metadata);
    }

}

