package com.fit.fit_be.domain.board.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum Weather {
    SUNNY("sunny"),
    CLOUDY("cloudy"),
    SNOWY("snowy"),
    RAIN("rain");

    private final String weather;

    Weather(String weather) {
        this.weather = weather;
    }

    @JsonCreator
    public static Weather of(String weather) {
        return Arrays.stream(Weather.values())
                .filter(Weather -> Objects.equals(Weather.getWeather(), weather))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @JsonValue
    public String getWeather() {
        return weather;
    }
}
