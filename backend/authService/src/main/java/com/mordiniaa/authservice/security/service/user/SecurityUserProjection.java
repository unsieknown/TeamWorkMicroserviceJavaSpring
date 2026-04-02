package com.mordiniaa.authservice.security.service.user;


import com.mordiniaa.authservice.security.model.user.AppRole;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class SecurityUserProjection {

    UUID userId;
    String username;
    String password;
    AppRole appRole;
    boolean accountNonExpired;
    boolean accountNonLocked;
    boolean credentialsNonExpired;
    boolean deleted;
}
