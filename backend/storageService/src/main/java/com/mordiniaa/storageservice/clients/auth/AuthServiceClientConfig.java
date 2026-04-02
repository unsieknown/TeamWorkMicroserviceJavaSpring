package com.mordiniaa.storageservice.clients.auth;

import com.mordiniaa.storageservice.config.InterserviceProperties;
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
    public RestClient.Builder plainBuilder() {
        return RestClient.builder();
    }

    @Bean
    public AuthServiceClient authClient(RestClient.Builder plainBuilder, InterserviceProperties interserviceProperties) {
        RestClient client = plainBuilder.baseUrl(
                interserviceProperties.getAuthService()
        ).build();

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(AuthServiceClient.class);
    }
}
