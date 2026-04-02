package com.mordiniaa.bordservice.utils;

import java.util.List;
import java.util.UUID;

public record JwtPrincipal(
        UUID userId,
        UUID sessionId,
        List<String> roles
) {
}
