package com.fit.fit_be.global.common.converter;

import com.fit.fit_be.domain.board.model.RoadCondition;
import org.springframework.core.convert.converter.Converter;

public class StringToRoadConditionConverter implements Converter<String, RoadCondition> {

    @Override
    public RoadCondition convert(String source) {
        return RoadCondition.of(source);
    }
}
