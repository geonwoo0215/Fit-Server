package com.fit.fit_be.domain.weather.fixture;

import com.fit.fit_be.domain.weather.model.WeatherArea;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class WeatherAreaFixture {

    private static final Long AREA_CODE = 11111L;
    private static final String STEP1 = "서울특별시";
    private static final String STEP2 = "도봉구";
    private static final String STEP3 = "창제5동";
    private static final Integer GRIDX = 61;
    private static final Integer GRIDY = 129;

    public static WeatherArea createWeatherArea(Integer nx, Integer ny) {
        return WeatherArea.builder()
                .areaCode(AREA_CODE)
                .step1(STEP1)
                .step2(STEP2)
                .step3(STEP3)
                .gridX(GRIDX)
                .gridY(GRIDY)
                .build();
    }

    public static List<WeatherArea> createWeatherAreas() {
        return IntStream.range(0, 100)
                .mapToObj(i -> WeatherAreaFixture.createWeatherArea(i, i))
                .collect(Collectors.toList());
    }

}
