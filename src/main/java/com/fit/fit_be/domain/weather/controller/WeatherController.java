package com.fit.fit_be.domain.weather.controller;

import com.fit.fit_be.domain.weather.dto.response.TemperatureDto;
import com.fit.fit_be.domain.weather.dto.response.WeatherResponse;
import com.fit.fit_be.domain.weather.service.WeatherService;
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
    public ResponseEntity<ApiResponse<WeatherResponse>> findWeatherByPosition
            (
                    @RequestParam("nx") Integer nx,
                    @RequestParam("ny") Integer ny
            ) {
        WeatherResponse weatherResponse = weatherService.findByPosition(nx, ny);

        return ResponseEntity.ok(new ApiResponse<>(weatherResponse));

    }

    @GetMapping("/weather/temperature")
    public ResponseEntity<ApiResponse<TemperatureDto>> findTemperatureByTime
            (
                    @RequestParam("startTime") String start,
                    @RequestParam("endTime") String end
            ) {
        TemperatureDto temperatureDto = weatherService.findTemperatureByTime(start, end);

        return ResponseEntity.ok(new ApiResponse<>(temperatureDto));

    }

}
