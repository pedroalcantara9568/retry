package com.alquiteto.retry.client;


import com.alquiteto.retry.config.DaleClientConfig;
import com.alquiteto.retry.config.DaleConfig;
import com.alquiteto.retry.config.WebClientConfig;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.time.Duration;

@Component
@RefreshScope
@Slf4j
public class DaleClient {

    private final DaleClientConfig daleconfig;
    private final WebClient client;

    public DaleClient(DaleConfig daleconfig,
                      @Qualifier("clientDale") WebClient client) {
        this.client = client;
        this.daleconfig = daleconfig.getDale();
    }

    public Mono<String> getRandom() {
        var timeout = Duration.ofMillis(daleconfig.getTimeout());
        var firstAttemptDuration = Duration.ofMillis(daleconfig.getFirstAttemptDuration());
        var lastAttemptDuration = Duration.ofMillis(daleconfig.getLastAttemptDuration());

        return client
                .get()
                .uri(daleconfig.getGetRandom().getUrl())
                .retrieve()
                .onStatus(status -> !HttpStatus.OK.equals(status),
                        response -> {
                            log.info("dale");
                            return getMonoErrorThrowable(new RuntimeException("deuRuim"));
                        })
                .bodyToMono(String.class)
                .timeout(timeout, handleTimeoutException())
                .retryWhen(WebClientConfig.retryBackoffSpec(daleconfig.getMaxRetries(),
                        firstAttemptDuration,
                        lastAttemptDuration,
                        LoggerFactory.getLogger(this.getClass()),
                        "AIIIIIIIIIN NÃO RESPONDEU CERTO NUMERO DE TENTATIVA: {}"));
    }

    @NotNull
    private Mono<Throwable> getMonoErrorThrowable(Exception exception) {
        return Mono.defer(() -> Mono.error(exception));
    }

    @NotNull
    private Mono<String> handleTimeoutException() {
        return Mono.defer(() -> Mono.error(new RuntimeException("AIIIIIIIIIN NÃO CONSEGUI OBTER A RESPOSTA")));
    }
}


