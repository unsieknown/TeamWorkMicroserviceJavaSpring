package com.mordiniaa.userservice.services;

import com.mordiniaa.userservice.dto.UserDto;
import com.mordiniaa.userservice.exceptions.BadRequestException;
import com.mordiniaa.userservice.exceptions.UsersNotAvailableException;
import com.mordiniaa.userservice.mappers.UserMapper;
import com.mordiniaa.userservice.models.AppRole;
import com.mordiniaa.userservice.models.User;
import com.mordiniaa.userservice.projections.SecurityUserProjection;
import com.mordiniaa.userservice.repositories.UserRepository;
import com.mordiniaa.userservice.responses.interservice.UserInfoResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final UserMapper userMapper;

    public User getUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User Not Found"));
    }

//    @Transactional
//    public UserDto getUserDto(String username) {
//        return userRepository.findUserByUsernameAndDeletedFalse(username)
//                .map(user -> UserDto.builder()
//                        .userId(user.getUserId())
//                        .username(username)
//                        .imageKey(user.getImageKey())
//                        .email(user.getContact().getEmail())
//                        .build()
//                )
//                .orElseThrow(() -> new BadRequestException("User Not Found"));
//    }

    public User findNonDeletedUserAndAppRole(UUID userId, AppRole appRole) {
        return userRepository.findUserByUserIdAndDeletedFalseAndRole_AppRole(userId, appRole)
                .orElseThrow(() -> new BadRequestException("User Not Found"));
    }

    public void checkUserAvailability(UUID userId) {
        if (!userRepository.existsUserByUserIdAndDeletedFalse(userId)) {
            throw new BadRequestException("User Not Found");
        }
    }

    public boolean checkUsersAvailability(Set<UUID> ids) {
        return userRepository.existsUsersByUserIdIsInAndDeletedFalse(ids);
    }

    public Set<UserDto> batchUsers(Set<UUID> ids) {
        return userRepository.getBaseUsersProjections(ids)
                .stream()
                .map(proj -> {
                    UserDto dto = new UserDto();
                    dto.setUserId(proj.getUserId());
                    dto.setUsername(proj.getUsername());
                    dto.setImageKey(proj.getImageKey());
                    return dto;
                })
                .collect(Collectors.toSet());
    }

    public UserInfoResponse getUserInfo(String username) {

        User user = userRepository.findUserByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BadRequestException("User Not Found"));

        List<String> roles = List.of(user.getRole().getAppRole().toString());

        return UserInfoResponse.builder()
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getContact().getEmail())
                .accountNonLocked(user.isAccountNonLocked())
                .accountNonExpired(user.isAccountNonExpired())
                .credentialsNonExpired(user.isCredentialsNonExpired())
                .deleted(user.isDeleted())
                .credentialsExpiryDate(user.getCredentialsExpiryDate())
                .accountExpiryDate(user.getAccountExpiryDate())
                .roles(roles)
                .build();
    }

    @Transactional
    public void updatePasswordByUserId(UUID userId, String password) {
        userRepository.updatePasswordByUserId(userId, password);
    }

    public SecurityUserProjection getSecurityUserById(UUID userId) {
        return userRepository.findSecurityUserById(userId)
                .orElseThrow(() -> new UsersNotAvailableException("User Not Found Or Not Available"));
    }

    public SecurityUserProjection getSecurityUserByUsername(String username) {
        return userRepository.findSecurityUserByUsername(username)
                .orElseThrow(() -> new UsersNotAvailableException("User Not Found Or Not Available"));
    }
}
