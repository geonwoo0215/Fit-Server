package com.fit.fit_be.domain.weather.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.domain.weather.ForecastTime;
import com.fit.fit_be.domain.weather.fixture.WeatherFixture;
import com.fit.fit_be.domain.weather.model.Weather;
import com.fit.fit_be.domain.weather.model.WeatherCategory;
import com.fit.fit_be.domain.weather.repository.WeatherRepository;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.restdocs.payload.PayloadDocumentation;
import org.springframework.restdocs.request.RequestDocumentation;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WeatherControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    JavaMailSender javaMailSender;

    @Autowired
    WeatherRepository weatherRepository;

    @Test
    @Transactional
    void 좌표로_날씨_조회_API_성공() throws Exception {

        Integer nx = 50;
        Integer ny = 60;

        Weather weatherTMP = WeatherFixture.createWeather(nx, ny, WeatherCategory.TMP);
        Weather weatherPOP = WeatherFixture.createWeather(nx, ny, WeatherCategory.POP);
        Weather weatherSKY = WeatherFixture.createWeather(nx, ny, WeatherCategory.SKY);
        Weather weatherWSD = WeatherFixture.createWeather(nx, ny, WeatherCategory.WSD);
        Weather weatherPCP = WeatherFixture.createWeather(nx, ny, WeatherCategory.PCP);

        List<Weather> weathers = List.of(weatherTMP, weatherPOP, weatherSKY, weatherWSD, weatherPCP);
        weatherRepository.saveAll(weathers);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("nx", String.valueOf(nx));
        params.add("ny", String.valueOf(ny));

        mockMvc.perform(RestDocumentationRequestBuilders.get("/weather")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("weather-findBy-position",
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("nx").description("x 좌표"),
                                RequestDocumentation.parameterWithName("ny").description("y 좌표")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.tmp").type(JsonFieldType.STRING).description("1시간 기온"),
                                PayloadDocumentation.fieldWithPath("data.pop").type(JsonFieldType.STRING).description("강수확률"),
                                PayloadDocumentation.fieldWithPath("data.sky").type(JsonFieldType.STRING).description("하늘 상태"),
                                PayloadDocumentation.fieldWithPath("data.wsd").type(JsonFieldType.STRING).description("풍속"),
                                PayloadDocumentation.fieldWithPath("data.pcp").type(JsonFieldType.STRING).description("1시간 강수량")
                        )
                ));
    }

    @Test
    @Transactional
    void 주어진시간에서_최대_최소_온도_조회_API_성공() throws Exception {

        String startTime = "0000";
        String endTime = "0600";

        String baseDate = ForecastTime.FIVE_AM.getBaseDate();
        String baseTime = ForecastTime.FIVE_PM.getBaseTime();
        String fcstDate = ForecastTime.FIVE_AM.getBaseDate();
        String fcstTime = ForecastTime.FIVE_AM.getBaseTime();
        List<Weather> weathers = WeatherFixture.createWeathers(baseDate, baseTime, fcstDate, fcstTime);
        weatherRepository.saveAll(weathers);


        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("startTime", startTime);
        params.add("endTime", endTime);

        mockMvc.perform(RestDocumentationRequestBuilders.get("/weather/temperature")
                        .params(params)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andDo(MockMvcRestDocumentation.document("weather-find-temperature-by-time",
                        RequestDocumentation.queryParameters(
                                RequestDocumentation.parameterWithName("startTime").description("시작시간"),
                                RequestDocumentation.parameterWithName("endTime").description("종료시간")
                        ),
                        PayloadDocumentation.responseFields(
                                PayloadDocumentation.fieldWithPath("data.lowestTemperature").type(JsonFieldType.NUMBER).description("최저온도"),
                                PayloadDocumentation.fieldWithPath("data.highestTemperature").type(JsonFieldType.NUMBER).description("최고온도")
                        )
                ));
    }


}