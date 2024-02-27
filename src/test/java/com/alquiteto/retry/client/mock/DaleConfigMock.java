package com.alquiteto.retry.client.mock;

import com.alquiteto.retry.config.DaleClientConfig;
import com.alquiteto.retry.config.DaleConfig;
import com.alquiteto.retry.config.DalePathClientConfig;

public class DaleConfigMock {

    public static DaleConfig daleClientConfig() {

        var dale = new DaleClientConfig();
        var path = new DalePathClientConfig();
        path.setUrl("/random");
        dale.setUrl("http://localhost:8080");
        dale.setGetRandom(path);
        dale.setTimeout(10000L);
        dale.setMaxRetries(3L);
        dale.setFirstAttemptDuration(1000L);
        dale.setLastAttemptDuration(1000L);
        return new DaleConfig(dale);
    }
}
