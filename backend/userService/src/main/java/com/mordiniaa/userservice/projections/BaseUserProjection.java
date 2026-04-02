package com.mordiniaa.userservice.projections;

import java.util.UUID;

public interface BaseUserProjection {

    UUID getUserId();

    String getUsername();

    String getImageKey();
}
