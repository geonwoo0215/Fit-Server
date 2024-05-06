package com.fit.fit_be.global.client.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit.fit_be.global.client.dto.VilageFcstResponse;
import com.fit.fit_be.global.client.dto.VilageFcstResponses;
import com.fit.fit_be.global.client.dto.fixture.VilageFcstFixture;
import com.fit.fit_be.global.client.dto.response.WeatherResponse;
import com.fit.fit_be.global.client.service.WeatherService;
import com.fit.fit_be.global.client.webclient.service.WebClientService;
import io.netty.channel.ChannelOption;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.util.List;

@SpringBootTest
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class WeatherControllerTest {

    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    JavaMailSender javaMailSender;
    private WeatherService weatherService;
    private MockWebServer mockWebServer;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        WebClientService webClientService = new WebClientService(WebClient.builder()
                .baseUrl(mockWebServer.url("testUrl").toString())
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)))
                .build());

        weatherService = new WeatherService(webClientService);
    }

    @AfterEach
    void terminate() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void 기상예보API_호출() throws JsonProcessingException {


        Integer nx = 55;
        Integer ny = 127;

        VilageFcstResponse tmpVilageFcstResponse = VilageFcstFixture.createVilageFcstResponse("TMP", "14");
        VilageFcstResponse popVilageFcstResponse = VilageFcstFixture.createVilageFcstResponse("POP", "30");
        VilageFcstResponse skyVilageFcstResponse = VilageFcstFixture.createVilageFcstResponse("SKY", "4");
        VilageFcstResponse wsdVilageFcstResponse = VilageFcstFixture.createVilageFcstResponse("WSD", "3.8");

        List<VilageFcstResponse> items = List.of(tmpVilageFcstResponse, popVilageFcstResponse, skyVilageFcstResponse, wsdVilageFcstResponse);
        VilageFcstResponses vilageFcstResponses = VilageFcstFixture.createVilageFcstResponses(items);

        mockWebServer.enqueue(new MockResponse()
                .addHeader("Content-Type", "application/json")
                .setResponseCode(HttpStatus.OK.value())
                .setBody(objectMapper.writeValueAsString(vilageFcstResponses)));

        WeatherResponse weatherResponse = weatherService.getWeather(nx, ny);

        Assertions.assertThat(weatherResponse).hasFieldOrPropertyWithValue("tmp", "14")
                .hasFieldOrPropertyWithValue("pop", "30")
                .hasFieldOrPropertyWithValue("sky", "4")
                .hasFieldOrPropertyWithValue("wsd", "3.8");
    }


}