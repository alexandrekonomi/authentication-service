package com.konomi.authenticationservice.service;

import com.konomi.authenticationservice.dto.UserDto;
import org.springframework.http.ResponseEntity;

public interface UserService {
    ResponseEntity<?> signUpUser(UserDto userDto);
}
