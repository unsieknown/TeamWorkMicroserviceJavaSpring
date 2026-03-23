package com.mordiniaa.userservice.repositories.mysql;

import com.mordiniaa.userservice.models.mysql.Address;
import com.mordiniaa.userservice.models.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIdAndUser(Long id, User user);
}
