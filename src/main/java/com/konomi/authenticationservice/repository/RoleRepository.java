package com.konomi.authenticationservice.repository;

import com.konomi.authenticationservice.enums.RoleType;
import com.konomi.authenticationservice.model.RoleModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<RoleModel, Long> {
    Optional<RoleModel> findByRoleName(RoleType roleType);
}
