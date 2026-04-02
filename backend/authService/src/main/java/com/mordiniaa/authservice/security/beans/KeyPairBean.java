package com.mordiniaa.authservice.security.beans;

import com.mordiniaa.authservice.config.KeyProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.security.KeyPair;

@Component
public class KeyPairBean {

    @Bean
    public KeyPair keyPair(KeyProvider keyProvider) {
        return keyProvider.getKeyPair();
    }
}
