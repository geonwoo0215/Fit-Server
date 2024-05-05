package com.fit.fit_be.global.client.controller;

import com.fit.fit_be.domain.board.model.Weather;
import com.fit.fit_be.global.client.dto.request.WeatherRequest;
import com.fit.fit_be.global.client.webclient.service.WebClientService;
import com.fit.fit_be.global.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WebClientService webClientService;


    @GetMapping("/weather")
    public ResponseEntity<ApiResponse<Weather>> getWeather
            (
                    @RequestParam("nx") Integer nx,
                    @RequestParam("ny") Integer ny
            ) {

    }

}
