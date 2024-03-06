package com.fit.fit_be.domain.board.dto.request;

import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateBoardRequest {

    private String content;

    private Long lowestTemperature;

    private Long highestTemperature;

    private Boolean open;

    private Weather weather;

    private RoadCondition roadCondition;

    private Place place;

    public UpdateBoardRequest(String content, Long lowestTemperature, Long highestTemperature, Boolean open, Weather weather, RoadCondition roadCondition, Place place) {
        this.content = content;
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.open = open;
        this.weather = weather;
        this.roadCondition = roadCondition;
        this.place = place;
    }
}
