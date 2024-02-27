package com.alquiteto.retry.controller;


import com.alquiteto.retry.client.DaleClient;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController("/retry")
public class RetryController {

    private final DaleClient client;

    @GetMapping("/dale")
    public Mono<String> getDale() {

        return client.getRandom();
    }

}
