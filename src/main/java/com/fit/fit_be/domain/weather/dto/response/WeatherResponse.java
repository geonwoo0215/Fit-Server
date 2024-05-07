package com.fit.fit_be.domain.weather.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherResponse {

    private String tmp;

    private String pop;

    private String sky;

    private String wsd;

    @Builder
    public WeatherResponse(String tmp, String pop, String sky, String wsd) {
        this.tmp = tmp;
        this.pop = pop;
        this.sky = sky;
        this.wsd = wsd;
    }
}
