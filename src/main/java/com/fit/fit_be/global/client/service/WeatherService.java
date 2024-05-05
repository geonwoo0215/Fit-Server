package com.fit.fit_be.global.client.service;

import com.fit.fit_be.global.client.dto.VilageFcstResponse;
import com.fit.fit_be.global.client.dto.VilageFcstResponses;
import com.fit.fit_be.global.client.dto.request.WeatherRequest;
import com.fit.fit_be.global.client.dto.response.WeatherResponse;
import com.fit.fit_be.global.client.webclient.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClientService webClientService;

    public WeatherResponse getWeather(Integer nx, Integer) {
        LocalDateTime now = LocalDateTime.now();

        String baseDate = now.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String baseTime = now.format(DateTimeFormatter.ofPattern("HH00"));

        VilageFcstResponses vilageFcstResponses = webClientService.get(baseDate,baseTime,weatherRequest.getNx(),weatherRequest.getNy());

        List<VilageFcstResponse> items = vilageFcstResponses.getItems();

        Optional<VilageFcstResponse> tmp = items.stream().filter(item -> item.getCategory().equals("TMP"))
                .findFirst();

        Optional<VilageFcstResponse> pop = items.stream().filter(item -> item.getCategory().equals("POP"))
                .findFirst();

        Optional<VilageFcstResponse> sky = items.stream().filter(item -> item.getCategory().equals("SKY"))
                .findFirst();

        Optional<VilageFcstResponse> wsd = items.stream().filter(item -> item.getCategory().equals("WSD"))
                .findFirst();

        String tmpValue = tmp.get().getFcstValue();
        String popValue = pop.get().getFcstValue();
        String skyValue = sky.get().getFcstValue();
        String wsdValue = wsd.get().getFcstValue();

        return WeatherResponse.builder()
                .tmp(tmpValue)
                .pop(popValue)
                .sky(skyValue)
                .wsd(wsdValue)
                .build();
    }
}
