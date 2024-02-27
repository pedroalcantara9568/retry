package com.alquiteto.retry.controller;

import com.alquiteto.retry.client.DaleClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.autoconfigure.RefreshAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Mono;

import static org.mockito.Mockito.when;

@WebFluxTest(controllers = RetryController.class)
@ImportAutoConfiguration(RefreshAutoConfiguration.class)
@ActiveProfiles("test")
public class RetryControllerTest {

    @MockBean
    private DaleClient mockClient;

    @Autowired
    public WebTestClient webClient;

    @Autowired
    public ObjectMapper mapper;

    @Test
    public void getDaleTest() {
        when(mockClient.getRandom()).thenReturn(Mono.just("Response"));
        webClient.get().uri("/dale")
                .exchange()
                .expectStatus()
                .isOk();
    }
}