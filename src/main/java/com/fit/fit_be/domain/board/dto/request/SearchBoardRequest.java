package com.fit.fit_be.domain.board.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SearchBoardRequest {

    private Long lowestTemperature;

    private Long highestTemperature;

    @Builder
    public SearchBoardRequest(Long lowestTemperature, Long highestTemperature) {
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
    }
}
