package com.fit.fit_be.domain.weather.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String baseDate;

    private String baseTime;

    @Enumerated(EnumType.STRING)
    private WeatherCategory category;

    private String fcstDate;

    private String fcstTime;

    private String fcstValue;

    private Integer nx;

    private Integer ny;

    @Builder
    public Weather(String baseDate, String baseTime, WeatherCategory category, String fcstDate, String fcstTime, String fcstValue, Integer nx, Integer ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.category = category;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
        this.nx = nx;
        this.ny = ny;
    }
}
