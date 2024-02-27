package com.alquiteto.retry.client.mock;

import okhttp3.mockwebserver.MockResponse;

public class ResponseMock {

    public static MockResponse buildResponse(Integer responseCode) {
        return new MockResponse()
                .setResponseCode(responseCode)
                .setBody("dale");
    }
}
