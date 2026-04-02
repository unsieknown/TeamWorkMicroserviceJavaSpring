package com.mordiniaa.userservice.projections;


import com.mordiniaa.userservice.models.AppRole;
import lombok.ToString;

import java.util.UUID;

public interface SecurityUserProjection {

    UUID getUserId();

    String getUsername();

    String getPassword();

    AppRole getAppRole();

    boolean getAccountNonExpired();

    boolean getAccountNonLocked();

    boolean getCredentialsNonExpired();

    boolean getDeleted();
}
