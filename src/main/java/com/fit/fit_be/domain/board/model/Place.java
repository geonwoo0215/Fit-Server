package com.fit.fit_be.domain.board.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum Place {

    WEDDING("결혼식"),
    OUTING("외출"),
    SPORTS("스포츠"),
    FESTIVAL("페스티발"),
    PARTY("파티");

    private final String place;

    Place(String place) {
        this.place = place;
    }

    @JsonCreator
    public static Place of(String place) {
        return Arrays.stream(Place.values())
                .filter(Place -> Objects.equals(Place.getPlace(), place))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @JsonValue
    public String getPlace() {
        return place;
    }
}
