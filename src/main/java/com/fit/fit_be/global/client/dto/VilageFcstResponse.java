package com.fit.fit_be.global.client.dto;

import lombok.Getter;

@Getter
public class VilageFcstResponse {

    private String baseDate;

    private String baseTime;

    private String category;

    private String fcstDate;

    private String fcstTime;

    private String fcstValue;

    private Integer nx;

    private Integer ny;
}
