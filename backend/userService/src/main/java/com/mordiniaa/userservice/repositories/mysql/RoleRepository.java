package com.mordiniaa.userservice.repositories.mysql;

import com.mordiniaa.userservice.models.mysql.AppRole;
import com.mordiniaa.userservice.models.mysql.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findRoleByAppRole(AppRole appRole);
}
