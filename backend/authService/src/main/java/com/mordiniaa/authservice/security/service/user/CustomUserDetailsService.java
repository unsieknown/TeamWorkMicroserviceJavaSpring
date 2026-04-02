package com.mordiniaa.authservice.security.service.user;

import com.mordiniaa.authservice.services.inter.UserServiceInter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserServiceInter userServiceInter;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        SecurityUserProjection user = userServiceInter.findSecurityUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        log.info("User: {}", user);
        return SecurityUser.build(user);
    }
}
