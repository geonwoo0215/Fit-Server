package com.fit.fit_be.domain.board.dto.request;

import com.fit.fit_be.domain.board.model.Place;
import com.fit.fit_be.domain.board.model.RoadCondition;
import com.fit.fit_be.domain.board.model.Weather;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchBoardRequest {

    private Long lowestTemperature;

    private Long highestTemperature;

    private Weather weather;

    private RoadCondition roadCondition;

    private Place place;

    private boolean mine;

    @Builder
    public SearchBoardRequest(Long lowestTemperature, Long highestTemperature, Weather weather, RoadCondition roadCondition, Place place, boolean mine) {
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
        this.weather = weather;
        this.roadCondition = roadCondition;
        this.place = place;
        this.mine = mine;
    }

    public static SearchBoardRequest.SearchBoardRequestBuilder searchBoardRequestBuilder(Long lowestTemperature, Long highestTemperature) {
        return SearchBoardRequest.builder()
                .lowestTemperature(lowestTemperature)
                .highestTemperature(highestTemperature);
    }
}
