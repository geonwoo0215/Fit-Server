package com.fit.fit_be.global.client.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class VilageFcstResponse {

    private String baseDate;

    private String baseTime;

    private String category;

    private String fcstDate;

    private String fcstTime;

    private String fcstValue;

    private Integer nx;

    private Integer ny;

    @Builder
    public VilageFcstResponse(String baseDate, String baseTime, String category, String fcstDate, String fcstTime, String fcstValue, Integer nx, Integer ny) {
        this.baseDate = baseDate;
        this.baseTime = baseTime;
        this.category = category;
        this.fcstDate = fcstDate;
        this.fcstTime = fcstTime;
        this.fcstValue = fcstValue;
        this.nx = nx;
        this.ny = ny;
    }
}
