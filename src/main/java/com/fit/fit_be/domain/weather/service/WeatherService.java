package com.fit.fit_be.domain.weather.service;

import com.fit.fit_be.domain.weather.ForecastTime;
import com.fit.fit_be.domain.weather.dto.response.TemperatureDto;
import com.fit.fit_be.domain.weather.dto.response.WeatherResponse;
import com.fit.fit_be.domain.weather.model.Weather;
import com.fit.fit_be.domain.weather.repository.WeatherAreaRepository;
import com.fit.fit_be.domain.weather.repository.WeatherRepository;
import com.fit.fit_be.global.client.dto.Item;
import com.fit.fit_be.global.client.webclient.service.WebClientService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeatherService {

    private final WebClientService webClientService;
    private final WeatherRepository weatherRepository;
    private final WeatherAreaRepository weatherAreaRepository;

    @Scheduled(cron = "0 0 2,5,8,11,14,17,20,23 * * *")
    @Transactional
    public void getWeather() {
        ForecastTime forecastTime = ForecastTime.of();

        List<Weather> weathers = weatherAreaRepository.findDistinctGridXY().stream()
                .map(grid -> webClientService.get(forecastTime.getBaseDate(), forecastTime.getBaseTime(), grid.getGridX(), grid.getGridY()))
                .flatMap(weatherFcstResponse -> weatherFcstResponse.getResponse().getBody().getItems().getItem().stream())
                .map(Item::toWeather)
                .toList();

        weatherRepository.saveAll(weathers);
    }

    @Transactional(readOnly = true)
    public WeatherResponse findByPosition(Integer nx, Integer ny) {
        List<Weather> weathers = weatherRepository.findWeatherByNxAndNy(nx, ny);

        String tmpValue = getWeatherValue(weathers, "tmp");
        String popValue = getWeatherValue(weathers, "pop");
        String skyValue = getWeatherValue(weathers, "sky");
        String wsdValue = getWeatherValue(weathers, "wsd");
        String pcpValue = getWeatherValue(weathers, "pcp");

        return WeatherResponse.builder()
                .tmp(tmpValue)
                .pop(popValue)
                .sky(skyValue)
                .wsd(wsdValue)
                .pcp(pcpValue)
                .build();
    }

    @Transactional(readOnly = true)
    public TemperatureDto findTemperatureByTime(String startTime, String endTime) {
        ForecastTime forecastTime = ForecastTime.of();
        List<String> temperatureValueByTime = weatherRepository
                .findTemperatureValueByTime(forecastTime.getBaseDate(), startTime, endTime);

        Integer max = temperatureValueByTime.stream()
                .map(Integer::parseInt)
                .max(Comparator.naturalOrder())
                .orElseThrow(RuntimeException::new);

        Integer min = temperatureValueByTime.stream()
                .map(Integer::parseInt)
                .min(Comparator.naturalOrder())
                .orElseThrow(RuntimeException::new);

        TemperatureDto temperatureDto = new TemperatureDto(min, max);

        return temperatureDto;
    }


    private String getWeatherValue(List<Weather> weathers, String category) {
        Optional<Weather> weatherOptional = weathers.stream()
                .filter(w -> w.getCategory().getCategory().equals(category))
                .findFirst();

        return weatherOptional.map(Weather::getFcstValue).orElseThrow(RuntimeException::new);
    }

}
