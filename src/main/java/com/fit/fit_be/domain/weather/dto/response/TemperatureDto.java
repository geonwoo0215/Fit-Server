package com.fit.fit_be.domain.weather.dto.response;

import lombok.Getter;

@Getter
public class TemperatureDto {

    private Integer lowestTemperature;

    private Integer highestTemperature;

    public TemperatureDto(Integer lowestTemperature, Integer highestTemperature) {
        this.lowestTemperature = lowestTemperature;
        this.highestTemperature = highestTemperature;
    }
}
