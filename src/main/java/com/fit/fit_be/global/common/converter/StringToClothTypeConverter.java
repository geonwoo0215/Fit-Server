package com.fit.fit_be.global.common.converter;


import com.fit.fit_be.domain.cloth.model.ClothType;
import org.springframework.core.convert.converter.Converter;

public class StringToClothTypeConverter implements Converter<String, ClothType> {

    @Override
    public ClothType convert(String source) {
        return ClothType.of(source);
    }
}
