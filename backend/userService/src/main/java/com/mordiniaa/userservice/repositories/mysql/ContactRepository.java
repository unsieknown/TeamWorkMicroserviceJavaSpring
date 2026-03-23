package com.mordiniaa.userservice.repositories.mysql;

import com.mordiniaa.userservice.models.mysql.Contact;
import com.mordiniaa.userservice.models.mysql.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    Optional<Contact> findByIdAndUser(Long id, User user);
}
