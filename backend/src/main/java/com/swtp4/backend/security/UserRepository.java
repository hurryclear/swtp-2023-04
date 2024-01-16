package com.swtp4.backend.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, String> {
    // retrieves details about the users from the database
    Optional<UserEntity> findByUsername(String username);
}
