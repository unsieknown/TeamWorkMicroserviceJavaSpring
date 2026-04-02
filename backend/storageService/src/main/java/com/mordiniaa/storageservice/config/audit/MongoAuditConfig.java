package com.mordiniaa.storageservice.config.audit;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@Configuration
@EnableMongoAuditing(auditorAwareRef = "mongoAuditor")
public class MongoAuditConfig {
}
