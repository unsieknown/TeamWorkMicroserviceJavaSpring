package com.mordiniaa.userservice.repositories;

import com.mordiniaa.userservice.models.AppRole;
import com.mordiniaa.userservice.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByAppRole(AppRole appRole);
}
