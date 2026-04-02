package com.mordiniaa.teamservice.clients.user;

import com.mordiniaa.teamservice.clients.BaseRestClientBuilder;
import com.mordiniaa.teamservice.config.InterserviceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class UserServiceClientConfig {

    @Bean
    public UserServiceClient getUserServiceRestClient(
            BaseRestClientBuilder baseRestClientBuilder,
            RestClient.Builder interceptedBuilder,
            InterserviceProperties interserviceProperties
    ) {

        RestClient client = baseRestClientBuilder.getBaseRestClient(
                interceptedBuilder,
                interserviceProperties.getUserService()
        );

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(UserServiceClient.class);
    }
}