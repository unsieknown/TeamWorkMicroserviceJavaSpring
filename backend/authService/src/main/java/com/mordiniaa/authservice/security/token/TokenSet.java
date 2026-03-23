package com.mordiniaa.authservice.security.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenSet {

    private JwtToken jwtToken;
    private RefreshToken refreshToken;
}
