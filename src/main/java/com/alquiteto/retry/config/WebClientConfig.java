package com.alquiteto.retry.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration

public class WebClientConfig {

    @Bean
    public WebClient clientDale(
            WebClient.Builder builder,
            @Value("${services.dale.url}") String baseUrl,
            @Value("${services.dale.get-random.url}") String path,
            @Value("${services.dale.connect-timeout:10000}") Integer connectTimeout,
            @Value("${services.dale.read-timeout:10000}") Integer readTimeout,
            @Value("${services.dale.write-timeout:10000}") Integer writeTimeout,
            @Value("${services.dale.max-connection:500}") Integer maxConnection
    ) {
        return getWebClient(builder, baseUrl, connectTimeout, readTimeout, writeTimeout, maxConnection, path);
    }


    private WebClient getWebClient(
            WebClient.Builder builder,
            String baseUrl,
            Integer connectTimeout,
            Integer readTimeout,
            Integer writeTimeout,
            Integer maxConnection,
            String path
    ) {
        return builder
                .uriBuilderFactory(uriBuilderFactory(baseUrl))
                .clientConnector(clientHttpConnector(connectTimeout, readTimeout, writeTimeout, maxConnection, path))
                .build();
    }

    private ClientHttpConnector clientHttpConnector(
            Integer connectTimeout,
            Integer readTimeout,
            Integer writeTimeout,
            Integer maxConnection,
            String path) {
        var httpClient = HttpClient.create(ConnectionProvider.create(path, maxConnection))
                .wiretap(false)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
                .doOnConnected(conn -> conn
                        .addHandlerLast(new ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                        .addHandlerLast(new WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS)));

        return new ReactorClientHttpConnector(httpClient);
    }

    public static Retry retryBackoffSpec(Long maxRetries,
                                         Duration firstAttemptDuration,
                                         Duration lastAttemptDuration,
                                         Logger logger,
                                         String retryMessage) {
        return Retry.backoff(maxRetries, firstAttemptDuration)
                .maxBackoff(lastAttemptDuration)
                .filter(throwable -> throwable instanceof RuntimeException)
                .doAfterRetry(it -> logger.info(retryMessage, it.totalRetries() + 1))
                .onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> retrySignal.failure());
    }

    private DefaultUriBuilderFactory uriBuilderFactory(String baseUrl) {
        return new DefaultUriBuilderFactory(baseUrl);
    }

}


























