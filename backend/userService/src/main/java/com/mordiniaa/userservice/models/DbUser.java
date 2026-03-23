package com.mordiniaa.userservice.models;

import java.util.UUID;

public interface DbUser {

    UUID getUserId();

    String getUsername();

    String getImageKey();
}
