package com.fit.fit_be.global.client.dto;

import com.fit.fit_be.domain.weather.model.Weather;
import com.fit.fit_be.domain.weather.model.WeatherCategory;
import lombok.Getter;

@Getter
public class Item {

    private String baseDate;
    private String baseTime;
    private String category;
    private String fcstDate;
    private String fcstTime;
    private String fcstValue;
    private int nx;
    private int ny;


    public Weather toWeather() {
        return Weather.builder()
                .baseDate(baseDate)
                .baseTime(baseTime)
                .category(WeatherCategory.of(category))
                .fcstDate(fcstDate)
                .fcstTime(fcstTime)
                .fcstValue(fcstValue)
                .nx(nx)
                .ny(ny)
                .build();

    }

}
