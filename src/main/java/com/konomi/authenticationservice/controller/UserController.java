package com.konomi.authenticationservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.konomi.authenticationservice.dto.ApiDto;
import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> signUpUser(@RequestBody @Validated({UserDto.UserView.SignupUser.class})
                                        @JsonView({UserDto.UserView.SignupUser.class}) UserDto userDto) {
        userService.signUpUser(userDto);
        return new ResponseEntity<>(new ApiDto("User account created successfully"), HttpStatus.CREATED);
    }
}
