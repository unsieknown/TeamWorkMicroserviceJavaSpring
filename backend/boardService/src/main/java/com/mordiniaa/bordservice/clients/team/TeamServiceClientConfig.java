package com.mordiniaa.bordservice.clients.team;

import com.mordiniaa.bordservice.clients.BaseRestClientBuilder;
import com.mordiniaa.bordservice.config.InterserviceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class TeamServiceClientConfig {

    @Bean
    public TeamServiceClient getTeamServiceClient(
            RestClient.Builder interceptedBuilder,
            BaseRestClientBuilder baseRestClientBuilder,
            InterserviceProperties interserviceProperties
    ) {

        RestClient client = baseRestClientBuilder.getBaseRestClient(
                interceptedBuilder,
                interserviceProperties.getTeamService()
        );

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(TeamServiceClient.class);
    }
}
