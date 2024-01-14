package com.fit.fit_be.domain.board.dto.response;

import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import lombok.Builder;
import lombok.Getter;

@Getter
public class BoardResponse {

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

    @Builder
    public BoardResponse(String content, Long lowestTemperature, Long highestTemperature, boolean open, Weather weather, RoadCondition roadCondition) {
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.weather = weather;
        this.roadCondition = roadCondition;
    }
}
