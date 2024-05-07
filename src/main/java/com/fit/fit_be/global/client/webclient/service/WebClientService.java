package com.fit.fit_be.global.client.webclient.service;

import com.fit.fit_be.global.client.dto.WeatherFcstResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class WebClientService {

    private final WebClient webClient;
    @Value("${weather.service-key}")
    private String serviceKey;

    public WeatherFcstResponse get(String baseDate, String baseTime, int nx, int ny) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("numOfRows", 12)
                        .queryParam("pageNo", 1)
                        .queryParam("dataType", "JSON")
                        .queryParam("base_date", baseDate)
                        .queryParam("base_time", baseTime)
                        .queryParam("nx", nx)
                        .queryParam("ny", ny)
                        .build())
                .retrieve()
                .bodyToMono(WeatherFcstResponse.class)
                .block();

    }

}
