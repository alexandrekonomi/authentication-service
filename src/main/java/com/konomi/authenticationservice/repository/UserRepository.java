package com.konomi.authenticationservice.repository;

import com.konomi.authenticationservice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByEmail(String email);

    boolean existsByDocument(String document);

    @Query("SELECT u FROM UserModel u JOIN FETCH u.roles WHERE u.email = ?1")
    Optional<UserModel> findByEmail(String email);
}
