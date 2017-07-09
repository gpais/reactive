package com.example.shoppingservice.cfg;


import java.io.IOException;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import com.google.common.io.ByteStreams;

public class LoggingRequestInterceptor  implements ClientHttpRequestInterceptor {

    private final static Logger log = LoggerFactory.getLogger(LoggingRequestInterceptor.class);

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        ClientHttpResponse response = execution.execute(request, body);

        log.debug("request method: {}, request headers: {}, request body: {}, response status code: {}, response headers: {}, response body: {}",
            request.getMethod(),
            request.getHeaders(),
            new String(body, Charset.forName("UTF-8")),
            response.getStatusCode(),
            response.getHeaders(),
            new String(ByteStreams.toByteArray(response.getBody()), Charset.forName("UTF-8")));

        return response;
    }


}