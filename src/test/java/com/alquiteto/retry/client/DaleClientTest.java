package com.alquiteto.retry.client;

import com.alquiteto.retry.client.mock.DaleConfigMock;
import com.alquiteto.retry.client.mock.ResponseMock;
import com.alquiteto.retry.config.WebClientConfig;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
public class DaleClientTest {


    public static final String DALE = "dale";
    private MockWebServer mockBackEnd;
    private DaleClient client;

    @BeforeEach
    public void setUp() throws IOException {
        mockBackEnd = new MockWebServer();
        mockBackEnd.start();
        var clientConfig = new WebClientConfig()
                .clientDale(WebClient.builder(),
                        mockBackEnd.url("/").toString(),
                        "/random",
                        10000,
                        10000,
                        5000,
                        50);
        client = new DaleClient(DaleConfigMock.daleClientConfig(), clientConfig);

    }

    @AfterEach
    void tearDown() throws IOException {
        mockBackEnd.shutdown();
    }

    @Test
    public void testGetRandom_validResponse() {
        mockBackEnd.enqueue(ResponseMock.buildResponse(200));
        assertDoesNotThrow(() -> client.getRandom());
    }

    @Test
    public void testGetRandom_retry() {
        mockBackEnd.enqueue(ResponseMock.buildResponse(500));
        mockBackEnd.enqueue(ResponseMock.buildResponse(500));
        mockBackEnd.enqueue(ResponseMock.buildResponse(200));
        StepVerifier.create(client.getRandom())
                .consumeNextWith(it -> Assertions.assertEquals(DALE, it))
                .verifyComplete();
    }

    @Test
    public void testGetRandom_retryExausted() {
        mockBackEnd.enqueue(ResponseMock.buildResponse(500));
        mockBackEnd.enqueue(ResponseMock.buildResponse(500));
        mockBackEnd.enqueue(ResponseMock.buildResponse(500));
        mockBackEnd.enqueue(ResponseMock.buildResponse(500));
        StepVerifier.create(client.getRandom())
                .expectErrorMatches(RuntimeException.class::isInstance)
                .verify();
    }
}