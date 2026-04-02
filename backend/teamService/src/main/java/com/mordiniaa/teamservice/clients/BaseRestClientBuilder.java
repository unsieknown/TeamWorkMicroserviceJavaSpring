package com.mordiniaa.teamservice.clients;

import com.mordiniaa.teamservice.exceptions.BadRequestException;
import com.mordiniaa.teamservice.exceptions.UnexpectedException;
import com.mordiniaa.teamservice.responses.APIExceptionResponse;
import com.mordiniaa.teamservice.utils.ApiResponseUtils;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.common.errors.ResourceNotFoundException;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class BaseRestClientBuilder {

    private final ApiResponseUtils apiResponseUtils;
    private final ServiceTokenProvider serviceTokenProvider;

    @Bean
    @LoadBalanced
    public RestClient.Builder interceptedBuilder() {
        return RestClient.builder();
    }

    public RestClient getBaseRestClient(RestClient.Builder builder, String service) {
        return builder.baseUrl(service)
                .requestInterceptor((request, body, execution) -> {

                    if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                        String token = serviceTokenProvider.getToken();
                        request.getHeaders().setBearerAuth(token);
                    }

                    if (!request.getHeaders().containsKey("X-User-Token")) {
                        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

                        if (auth instanceof JwtAuthenticationToken authToken) {
                            String userToken = authToken.getToken().getTokenValue();
                            request.getHeaders().add("X-User-Token", userToken);
                        }
                    }
                    return execution.execute(request, body);
                })
                .defaultStatusHandler(status -> status.value() == 404, (request, response) -> {
                    throw new ResourceNotFoundException("Resource Not Found");
                })
                .defaultStatusHandler(status -> status.value() == 400, (request, response) -> {
                    APIExceptionResponse error = apiResponseUtils.readError(response);
                    throw new BadRequestException(error.getMessage());
                })
                .defaultStatusHandler(HttpStatusCode::is4xxClientError, (req, res) -> {
                    throw new BadRequestException("Client error: " + res.getStatusCode());
                })
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, ((request, response) -> {
                    APIExceptionResponse error = apiResponseUtils.readError(response);
                    throw new UnexpectedException(error.getMessage());
                }))
                .build();
    }
}
