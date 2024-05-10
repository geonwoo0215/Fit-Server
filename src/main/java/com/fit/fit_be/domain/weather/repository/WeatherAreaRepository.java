package com.fit.fit_be.domain.weather.repository;

import com.fit.fit_be.domain.weather.dto.response.GridDto;
import com.fit.fit_be.domain.weather.model.WeatherArea;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WeatherAreaRepository extends JpaRepository<WeatherArea, Long> {

    @Query("SELECT distinct NEW com.fit.fit_be.domain.weather.dto.response.GridDto(wa.gridX, wa.gridY) FROM WeatherArea wa")
    List<GridDto> findDistinctGridXY();


}
