package com.fit.fit_be.global.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;


@Configuration
public class WebClientConfig {

    @Bean
    public DefaultUriBuilderFactory defaultUriBuilderFactory() {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory("http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getVilageFcst");
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);
        return factory;
    }

    @Bean
    public WebClient webClient(HttpClient httpClient, DefaultUriBuilderFactory defaultUriBuilderFactory) {
        return WebClient.builder()
                .uriBuilderFactory(defaultUriBuilderFactory)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    @Bean
    public ConnectionProvider connectionProvider() {
        return ConnectionProvider.builder("http-connection-pool")
                .maxConnections(100)
                .maxIdleTime(Duration.ofMillis(5000))
                .maxLifeTime(Duration.ofMillis(5000))
                .pendingAcquireTimeout(Duration.ofMillis(5000))
                .pendingAcquireMaxCount(-1)
                .build();
    }

    @Bean
    public HttpClient httpClient(ConnectionProvider connectionProvider) {
        return HttpClient.create(connectionProvider)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 10000)
                .responseTimeout(Duration.ofMillis(5000))
                .doOnConnected(conn ->
                        conn.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS))
                                .addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS)));
    }
}
