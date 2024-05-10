package com.fit.fit_be.domain.weather.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
public class WeatherResponse {

    private String tmp;

    private String pop;

    private String sky;

    private String wsd;

    private String pcp;

    @Builder
    public WeatherResponse(String tmp, String pop, String sky, String wsd, String pcp) {
        this.tmp = tmp;
        this.pop = pop;
        this.sky = sky;
        this.wsd = wsd;
        this.pcp = pcp;
    }
}
