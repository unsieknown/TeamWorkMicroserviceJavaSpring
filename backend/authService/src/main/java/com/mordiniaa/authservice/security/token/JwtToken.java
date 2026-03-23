package com.mordiniaa.authservice.security.token;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JwtToken implements Token {

    private String tokenName;
    private String token;
    private long ttl;
}
