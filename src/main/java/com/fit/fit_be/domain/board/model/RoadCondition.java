package com.fit.fit_be.domain.board.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;
import java.util.Objects;

public enum RoadCondition {

    NORMAL("평범한"),
    SLIPPERY("미끄러운"),
    SNOW("눈길"),
    SAND("모래");

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
