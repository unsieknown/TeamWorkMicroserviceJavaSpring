package com.mordiniaa.userservice.projections;


import com.mordiniaa.userservice.models.mysql.Role;

import java.util.UUID;

public interface SecurityUserProjection {

    UUID getUserId();

    String getUsername();

    String getPassword();

    Role getRole();

    boolean getAccountNonExpired();

    boolean getAccountNonLocked();

    boolean getCredentialsNonExpired();

    boolean getDeleted();
}
