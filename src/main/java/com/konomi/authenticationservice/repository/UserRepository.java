package com.konomi.authenticationservice.repository;

import com.konomi.authenticationservice.model.UserModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRepository extends JpaRepository<UserModel, UUID> {
    boolean existsByEmail(String email);

    boolean existsByDocument(String document);
}
