package com.konomi.authenticationservice.model;

import com.konomi.authenticationservice.enums.RoleType;
import jakarta.persistence.*;

@Entity
@Table(name = "tab_role")
public class RoleModel {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Enumerated(EnumType.STRING)
    @Column(name = "name")
    private RoleType roleName;
}
