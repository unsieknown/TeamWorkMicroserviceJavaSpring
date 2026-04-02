package com.mordiniaa.gatewayserver.security;

import com.mordiniaa.gatewayserver.security.exceptions.AccessDeniedExceptionHandler;
import com.mordiniaa.gatewayserver.security.exceptions.EntryPoint;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    SecurityWebFilterChain securityFilterChain(
            ServerHttpSecurity http,
            AccessDeniedExceptionHandler accessDeniedExceptionHandler,
            EntryPoint entryPoint,
            Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter, ReactiveJwtDecoder jwtDecoder) {

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                        .accessDeniedHandler(accessDeniedExceptionHandler)
                )

                // I know it is long but it has better readability
                .authorizeExchange(auth -> auth

                        .pathMatchers("/api/v1/auth/internal/token").denyAll()
                        .pathMatchers("/api/v1/auth/.well-known/jwks.json").denyAll()
                        .pathMatchers("/api/v1/auth/actuator/health").denyAll()
                        .pathMatchers("/api/v1/auth/signout").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")
                        .pathMatchers("/api/v1/auth/**").permitAll()

                        .pathMatchers("/api/v1/boards/internal/token").denyAll()
                        .pathMatchers("/api/v1/boards/actuator/health").denyAll()
                        .pathMatchers("/api/v1/boards/admin").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/api/v1/boards/manager").hasAuthority("ROLE_MANAGER")
                        .pathMatchers("/api/v1/boards/**").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")

                        .pathMatchers("/api/v1/tasks/internal/token").denyAll()
                        .pathMatchers("/api/v1/tasks/actuator/health").denyAll()
                        .pathMatchers("/api/v1/tasks/**").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")

                        .pathMatchers("/api/v1/notes/internal/token").denyAll()
                        .pathMatchers("/api/v1/notes/actuator/health").denyAll()
                        .pathMatchers("/api/v1/notes/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER")

                        .pathMatchers("/api/v1/storage/internal/token").denyAll()
                        .pathMatchers("/api/v1/storage/actuator/health").denyAll()
                        .pathMatchers("/api/v1/storage/resource/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER")
                        .pathMatchers("/api/v1/storage/images/**").permitAll()

                        .pathMatchers("/api/v1/teams/internal/token").denyAll()
                        .pathMatchers("/api/v1/teams/actuator/health").denyAll()
                        .pathMatchers("/api/v1/teams/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/api/v1/teams/manager/**").hasAuthority("ROLE_MANAGER")

                        .pathMatchers("/api/v1/users/internal/token").denyAll()
                        .pathMatchers("/api/v1/users/actuator/health").denyAll()
                        .pathMatchers("/api/v1/users/admin/**").hasAuthority("ROLE_ADMIN")
                        .pathMatchers("/api/v1/users/**").hasAnyAuthority("ROLE_USER", "ROLE_MANAGER", "ROLE_ADMIN")

                        .anyExchange().denyAll()
                )

                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter)
                        )
                )
                .build();
    }

    @Bean
    public ReactiveJwtDecoder jwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}") String jwtUri
    ) {

        NimbusReactiveJwtDecoder decoder = NimbusReactiveJwtDecoder
                .withJwkSetUri(jwtUri)
                .build();

        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer("auth-service");
        decoder.setJwtValidator(withIssuer);

        return decoder;
    }

    @Bean
    public Converter<Jwt, ? extends Mono<? extends AbstractAuthenticationToken>> jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter gac = new JwtGrantedAuthoritiesConverter();
        gac.setAuthoritiesClaimName("roles");
        gac.setAuthorityPrefix("");

        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();
        delegate.setJwtGrantedAuthoritiesConverter(gac);

        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }
}
