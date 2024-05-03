package com.fit.fit_be.global.client.webclient.service;

import com.fit.fit_be.global.client.dto.VilageFcstResponses;
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

    public VilageFcstResponses get() {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/getVilageFcst")
                        .queryParam("serviceKey", serviceKey)
                        .queryParam("numOfRows", 12)
                        .queryParam("pageNo", 1)
                        .queryParam("dataType", "JSON")
                        .queryParam("base_date")
                        .queryParam("base_time")
                        .queryParam("nx")
                        .queryParam("ny")
                        .build())
                .retrieve()
                .bodyToMono(VilageFcstResponses.class)
                .block();

    }

}
