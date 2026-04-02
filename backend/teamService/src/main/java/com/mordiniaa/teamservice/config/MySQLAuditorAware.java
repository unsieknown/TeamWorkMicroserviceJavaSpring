package com.mordiniaa.teamservice.config;

import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("mysqlAuditor")
public class MySQLAuditorAware implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("MYSQL_AUDITOR");
    }
}
