package com.fit.fit_be.domain.weather.repository;

import com.fit.fit_be.domain.weather.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {

    List<Weather> findWeatherByNxAndNy(@Param("nx") Integer nx, @Param("ny") Integer ny);

    @Query(value = "SELECT w.fcstValue " +
            "FROM Weather w " +
            "WHERE w.category = 'tmp' " +
            "AND w.fcstDate = :baseDate " +
            "AND w.fcstTime BETWEEN :startTime AND :endTime")
    List<String> findTemperatureValueByTime(@Param("baseDate") String baseDate, @Param("startTime") String startTime, @Param("endTime") String endTime);
}
