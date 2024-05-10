package com.fit.fit_be.domain.weather.repository;

import com.fit.fit_be.config.DataLoader;
import com.fit.fit_be.config.TestConfig;
import com.fit.fit_be.domain.weather.ForecastTime;
import com.fit.fit_be.domain.weather.fixture.WeatherFixture;
import com.fit.fit_be.domain.weather.model.Weather;
import com.fit.fit_be.domain.weather.model.WeatherCategory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;

@DataJpaTest
@Import({TestConfig.class, DataLoader.class})
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WeatherRepositoryTest {

    @Autowired
    WeatherRepository weatherRepository;

    @Test
    void x좌표와_y좌표로_날씨조회() {

        Integer nx = 50;
        Integer ny = 60;

        Weather weather = WeatherFixture.createWeather(nx, ny, WeatherCategory.TMP);
        weatherRepository.save(weather);

        List<Weather> weathers = weatherRepository.findWeatherByNxAndNy(nx, ny);

        Weather findWeather = weathers.get(0);
        Assertions.assertThat(weather).isEqualTo(findWeather);
    }

    @Test
    void 지정한_시간의_최저_최고_온도_조회() {

        String baseDate = ForecastTime.FIVE_AM.getBaseDate();
        String baseTime = ForecastTime.FIVE_PM.getBaseTime();
        String fcstDate = ForecastTime.FIVE_AM.getBaseDate();
        String fcstTime = ForecastTime.FIVE_AM.getBaseTime();
        List<Weather> weathers = WeatherFixture.createWeathers(baseDate, baseTime, fcstDate, fcstTime);
        weatherRepository.saveAll(weathers);

        String startTime = "0000";
        String endTime = "0600";

        List<String> temperatureValueByTime = weatherRepository.findTemperatureValueByTime(baseDate, startTime, endTime);
        Assertions.assertThat(temperatureValueByTime.size()).isEqualTo(weathers.size());
    }
}