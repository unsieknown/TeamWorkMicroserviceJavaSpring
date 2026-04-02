package com.mordiniaa.userservice.repositories;

import com.mordiniaa.userservice.models.AppRole;
import com.mordiniaa.userservice.models.User;
import com.mordiniaa.userservice.projections.BaseUserProjection;
import com.mordiniaa.userservice.projections.SecurityUserProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Modifying
    @Query("update User u set u.imageKey = :imageKey where u.userId = :userId")
    void setProfileImageKey(UUID userId, String imageKey);

    boolean existsUserByFirstNameAndLastName(String firstName, String lastName);

    boolean existsByUsername(String username);

    @Modifying
    @Query("update User u set u.deleted = :deleted where u.userId = :userId")
    void updateDeletedByUserId(boolean deleted, UUID userId);

    @Query("select count(*) from User u where u.role.appRole = :appRole")
    int countByRole_AppRole(AppRole appRole);

    Optional<User> findUsersByRole_AppRole(AppRole roleAppRole);

    @Query("""
            select u.userId as userId,
            u.username as username,
            u.password as password,
            u.role.appRole as appRole,
            u.accountNonExpired as accountNonExpired,
            u.accountNonLocked as accountNonLocked,
            u.credentialsNonExpired as credentialsNonExpired,
            u.deleted as deleted
            from User u
            where u.username = :username
            """)
    Optional<SecurityUserProjection> findSecurityUserByUsername(String username);

    @Query("""
            select u.userId as userId,
            u.username as username,
            u.password as password,
            u.role.appRole as appRole,
            u.accountNonExpired as accountNonExpired,
            u.accountNonLocked as accountNonLocked,
            u.credentialsNonExpired as credentialsNonExpired,
            u.deleted as deleted
            from User u
            where u.userId = :userId
            """)
    Optional<SecurityUserProjection> findSecurityUserById(UUID userId);

    @Modifying
    @Query("""
            update User u
            set u.password = :newPassword
            where u.userId = :userId
            """)
    void updatePasswordByUserId(UUID userId, String newPassword);

    Optional<User> findUserByUserIdAndDeletedFalse(UUID userId);

    Optional<User> findUserByUserIdAndDeletedFalseAndRole_AppRole(UUID userId, AppRole roleAppRole);

    Optional<User> findUserByUsernameAndDeletedFalse(String username);

    boolean existsUserByUserIdAndDeletedFalse(UUID userId);

    @Query("""
            select
                u.userId as userId,
                u.username as username,
                u.imageKey as imageKey
            from User u where u.userId in :ids
            """)
    Set<BaseUserProjection> getBaseUsersProjections(Set<UUID> ids);

    boolean existsUsersByUserIdIsInAndDeletedFalse(Collection<UUID> userIds);
}
