package com.fit.fit_be.domain.weather.dto.response;

import lombok.Getter;

@Getter
public class GridDto {

    private Integer gridX;

    private Integer gridY;

    public GridDto(Integer gridX, Integer gridY) {
        this.gridX = gridX;
        this.gridY = gridY;
    }
}
