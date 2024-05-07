package com.fit.fit_be.domain.weather.service;

import com.fit.fit_be.domain.weather.ForecastTime;
import com.fit.fit_be.domain.weather.dto.response.WeatherResponse;
import com.fit.fit_be.global.client.dto.Item;
import com.fit.fit_be.global.client.dto.WeatherFcstResponse;
import com.fit.fit_be.global.client.webclient.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClientService webClientService;

    public WeatherResponse getWeather(Integer nx, Integer ny) {

        ForecastTime forecastTime = ForecastTime.of();
        WeatherFcstResponse weatherFcstResponse = webClientService.get(forecastTime.getBaseDate(), forecastTime.getBaseTime(), nx, ny);


        List<Item> item = weatherFcstResponse.getResponse().getBody().getItems().getItem();

        Optional<Item> tmp = item.stream().filter(i -> i.getCategory().equals("TMP"))
                .findFirst();

        Optional<Item> pop = item.stream().filter(i -> i.getCategory().equals("POP"))
                .findFirst();

        Optional<Item> sky = item.stream().filter(i -> i.getCategory().equals("SKY"))
                .findFirst();

        Optional<Item> wsd = item.stream().filter(i -> i.getCategory().equals("WSD"))
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
