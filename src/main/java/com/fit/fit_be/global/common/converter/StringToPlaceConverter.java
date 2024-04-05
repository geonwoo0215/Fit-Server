package com.fit.fit_be.global.common.converter;

import com.fit.fit_be.domain.board.model.Place;
import org.springframework.core.convert.converter.Converter;

public class StringToPlaceConverter implements Converter<String, Place> {

    @Override
    public Place convert(String source) {
        return Place.of(source);
    }
}
