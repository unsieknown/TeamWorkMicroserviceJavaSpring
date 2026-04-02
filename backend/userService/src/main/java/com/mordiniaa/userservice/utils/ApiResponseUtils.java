package com.mordiniaa.userservice.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mordiniaa.userservice.exceptions.UnexpectedException;
import com.mordiniaa.userservice.responses.APIExceptionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;

import java.io.InputStream;

@Component
@RequiredArgsConstructor
public class ApiResponseUtils {

    private final ObjectMapper objectMapper;

    public APIExceptionResponse readError(ClientHttpResponse response) {
        try (InputStream is = response.getBody()) {
            return objectMapper.readValue(is, APIExceptionResponse.class);
        } catch (Exception e) {
            throw new UnexpectedException("Unknow Error Happened");
        }
    }
}
