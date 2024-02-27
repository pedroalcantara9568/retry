package com.alquiteto.retry.client.mock;

import okhttp3.mockwebserver.MockResponse;

public class ResponseMock {

    public static MockResponse buildResponse(Integer responseCode) {
        return new MockResponse()
                .setResponseCode(responseCode)
                .setBody("""
                        {
                            "error": "Could not find a required Access Token in the request, identified by HEADER access_token"
                        }""");
    }
}
