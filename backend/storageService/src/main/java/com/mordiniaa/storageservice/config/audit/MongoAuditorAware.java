package com.mordiniaa.storageservice.config.audit;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("mongoAuditor")
public class MongoAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("MONGO_AUDITOR");
    }
}
