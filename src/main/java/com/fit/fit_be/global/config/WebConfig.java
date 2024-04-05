package com.fit.fit_be.global.config;

import com.fit.fit_be.global.common.converter.StringToClothTypeConverter;
import com.fit.fit_be.global.common.converter.StringToPlaceConverter;
import com.fit.fit_be.global.common.converter.StringToRoadConditionConverter;
import com.fit.fit_be.global.common.converter.StringToWeatherConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToClothTypeConverter());
        registry.addConverter(new StringToPlaceConverter());
        registry.addConverter(new StringToRoadConditionConverter());
        registry.addConverter(new StringToWeatherConverter());
    }
}
