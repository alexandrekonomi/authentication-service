package com.konomi.authenticationservice.service;

import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface UserService {
    ResponseEntity<?> signUpUser(UserDto userDto);
    UserModel getById(UUID userId);
    Page<UserModel> findAll(Pageable pageable);
    ResponseEntity<?> inactive(UUID userId);
    ResponseEntity<?> update(UUID userId, UserDto userDto);
}
