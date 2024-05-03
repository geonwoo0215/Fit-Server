package com.fit.fit_be.global.config;

import io.netty.channel.ChannelOption;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        HttpClient httpClient = HttpClient.create().option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000);
        return WebClient.builder()
                .baseUrl("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
