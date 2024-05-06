package com.fit.fit_be.global.client.controller;

import com.fit.fit_be.global.client.dto.response.WeatherResponse;
import com.fit.fit_be.global.client.service.WeatherService;
import com.fit.fit_be.global.common.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WeatherController {

    private final WeatherService weatherService;
    
    @GetMapping("/weather")
    public ResponseEntity<ApiResponse<WeatherResponse>> getWeather
            (
                    @RequestParam("nx") Integer nx,
                    @RequestParam("ny") Integer ny
            ) {
        WeatherResponse weatherResponse = weatherService.getWeather(nx, ny);

        return ResponseEntity.ok(new ApiResponse<>(weatherResponse));

    }

}
