package com.fit.fit_be.domain.board.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum RoadCondition {

    SLIPPERY("미끄러운");

    private final String roadCondition;

    RoadCondition(String roadCondition) {
        this.roadCondition = roadCondition;
    }

    @JsonCreator
    public static RoadCondition of(String roadCondition) {
        return Arrays.stream(RoadCondition.values())
                .filter(Weather -> Objects.equals(Weather.getRoadCondition(), roadCondition))
                .findFirst()
                .orElseThrow(RuntimeException::new);
    }

    @JsonValue
    public String getRoadCondition() {
        return roadCondition;
    }

}
