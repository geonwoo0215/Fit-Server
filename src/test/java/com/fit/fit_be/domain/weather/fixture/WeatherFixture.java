package com.fit.fit_be.domain.weather.fixture;

import com.fit.fit_be.domain.weather.model.Weather;
import com.fit.fit_be.domain.weather.model.WeatherCategory;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeatherFixture {

    private static final String BASE_DATE = "20240510";
    private static final String BASE_TIME = "0200";
    private static final String FCST_DATE = "20240510@";
    private static final String FCST_TIME = "0200";
    private static final String FCST_VALUE = "14";
    private static final Integer GRIDX = 61;
    private static final Integer GRIDY = 129;

    public static Weather createWeather(String baseDate, String baseTime, String fcstDate, String fcstTime, String fcstValue, Integer nx, Integer ny) {
        return Weather.builder()
                .baseDate(baseDate)
                .baseTime(baseTime)
                .category(WeatherCategory.TMP)
                .fcstDate(fcstDate)
                .fcstTime(fcstTime)
                .fcstValue(fcstValue)
                .nx(nx)
                .ny(ny)
                .build();
    }

    public static List<Weather> createWeathers(String baseDate, String baseTime, String fcstDate, String fcstTime) {
        return IntStream.range(0, 100)
                .mapToObj(i -> WeatherFixture.createWeather(baseDate, baseTime, fcstDate, fcstTime, String.valueOf(i), GRIDX, GRIDY))
                .collect(Collectors.toList());
    }

    public static Weather createWeather(Integer nx, Integer ny, WeatherCategory weatherCategory) {
        return Weather.builder()
                .baseDate(BASE_DATE)
                .baseTime(BASE_TIME)
                .category(weatherCategory)
                .fcstDate(FCST_DATE)
                .fcstTime(FCST_TIME)
                .fcstValue(FCST_VALUE)
                .nx(nx)
                .ny(ny)
                .build();
    }

    public static List<Weather> createWeathers() {
        return IntStream.range(0, 100)
                .mapToObj(i -> WeatherFixture.createWeather(i, i, WeatherCategory.TMP))
                .collect(Collectors.toList());
    }
}
