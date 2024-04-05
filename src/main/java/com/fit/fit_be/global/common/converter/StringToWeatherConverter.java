package com.fit.fit_be.global.common.converter;

import com.fit.fit_be.domain.board.model.Weather;
import org.springframework.core.convert.converter.Converter;

public class StringToWeatherConverter implements Converter<String, Weather> {

    @Override
    public Weather convert(String source) {
        return Weather.of(source);
    }
}
