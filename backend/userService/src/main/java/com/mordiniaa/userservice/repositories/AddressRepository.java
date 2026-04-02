package com.mordiniaa.userservice.repositories;

import com.mordiniaa.userservice.models.Address;
import com.mordiniaa.userservice.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {

    Optional<Address> findByIdAndUser(Long id, User user);
}
