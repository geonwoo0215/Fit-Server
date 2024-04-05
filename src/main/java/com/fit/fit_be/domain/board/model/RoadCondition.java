package com.fit.fit_be.domain.board.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum RoadCondition {

    NORMAL("normal"),
    SLIPPERY("slippery"),
    SNOW("snow"),
    SAND("sand");

    private final String roadCondition;

    RoadCondition(String roadCondition) {
        this.roadCondition = roadCondition;
    }

    @JsonCreator
    public static RoadCondition of(String roadCondition) {
        return Arrays.stream(RoadCondition.values())
                .filter(RoadCondition -> Objects.equals(RoadCondition.getRoadCondition(), roadCondition))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @JsonValue
    public String getRoadCondition() {
        return roadCondition;
    }

}
