package com.alquiteto.retry.config;

import lombok.Data;

@Data
public class DaleClientConfig {
    private String url;
    private String accessToken;
    private DalePathClientConfig getRandom;
    private Long timeout;
    private Long maxRetries;
    private Long firstAttemptDuration;
    private Long lastAttemptDuration;
}
