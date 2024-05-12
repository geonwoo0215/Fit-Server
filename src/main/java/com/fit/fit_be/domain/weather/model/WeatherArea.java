package com.fit.fit_be.domain.weather.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "weather_area", indexes = @Index(name = "idx_gridx_gridy", columnList = "gridx, gridy"))
public class WeatherArea {

    @Id
    private Long areaCode;

    private String step1;

    private String step2;

    private String step3;

    private Integer gridX;

    private Integer gridY;

    @Builder
    public WeatherArea(Long areaCode, String step1, String step2, String step3, Integer gridX, Integer gridY) {
        this.areaCode = areaCode;
        this.step1 = step1;
        this.step2 = step2;
        this.step3 = step3;
        this.gridX = gridX;
        this.gridY = gridY;
    }
}
