package com.fit.fit_be.domain.weather.model;

import java.util.Arrays;
import java.util.Objects;

public enum WeatherCategory {

    TMP("tmp"),
    POP("pop"),
    SKY("sky"),
    WSD("wsd"),
    PCP("pcp");

    private final String category;

    WeatherCategory(String category) {
        this.category = category;
    }

    public static WeatherCategory of(String category) {
        return Arrays.stream(WeatherCategory.values())
                .filter(WeatherCategory -> Objects.equals(WeatherCategory.getCategory(), category))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    public String getCategory() {
        return category;
    }
}
