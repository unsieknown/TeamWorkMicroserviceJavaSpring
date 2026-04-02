package com.mordiniaa.teamservice.clients.auth;

import com.mordiniaa.teamservice.config.InterserviceProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class AuthServiceClientConfig {

    @Bean
    @LoadBalanced
    public RestClient.Builder planeBuilder() {
        return RestClient.builder();
    }

    @Bean
    public AuthServiceClient getAuthServiceClient(RestClient.Builder planeBuilder, InterserviceProperties interserviceProperties) {

        RestClient client = planeBuilder
                .baseUrl(interserviceProperties.getAuthService())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AuthServiceClient.class);
    }
}
