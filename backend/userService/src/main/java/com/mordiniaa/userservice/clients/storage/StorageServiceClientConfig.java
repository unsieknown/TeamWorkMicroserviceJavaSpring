package com.mordiniaa.userservice.clients.storage;

import com.mordiniaa.userservice.clients.BaseRestClientBuilder;
import com.mordiniaa.userservice.config.InterserviceProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Component
public class StorageServiceClientConfig {

    @Bean
    public StorageServiceClient getStorageServiceClient(
            RestClient.Builder interceptedBuilder,
            BaseRestClientBuilder baseRestClientBuilder, InterserviceProperties interserviceProperties) {

        RestClient client = baseRestClientBuilder.getBaseRestClient(
                interceptedBuilder,
                interserviceProperties.getStorageService()
        );

        RestClientAdapter adapter = RestClientAdapter.create(client);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory.builderFor(adapter).build();

        return factory.createClient(StorageServiceClient.class);
    }
}
