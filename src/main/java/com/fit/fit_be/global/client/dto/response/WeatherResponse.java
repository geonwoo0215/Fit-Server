package com.fit.fit_be.global.client.dto.response;

import lombok.Builder;

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
