package com.fit.fit_be.domain.weather.model;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QWeather is a Querydsl query type for Weather
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QWeather extends EntityPathBase<Weather> {

    private static final long serialVersionUID = -1593367874L;

    public static final QWeather weather = new QWeather("weather");

    public final StringPath baseDate = createString("baseDate");

    public final StringPath baseTime = createString("baseTime");

    public final EnumPath<WeatherCategory> category = createEnum("category", WeatherCategory.class);

    public final StringPath fcstDate = createString("fcstDate");

    public final StringPath fcstTime = createString("fcstTime");

    public final StringPath fcstValue = createString("fcstValue");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final NumberPath<Integer> nx = createNumber("nx", Integer.class);

    public final NumberPath<Integer> ny = createNumber("ny", Integer.class);

    public QWeather(String variable) {
        super(Weather.class, forVariable(variable));
    }

    public QWeather(Path<? extends Weather> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWeather(PathMetadata metadata) {
        super(Weather.class, metadata);
    }

}

