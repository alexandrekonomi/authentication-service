package com.konomi.authenticationservice.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.konomi.authenticationservice.dto.ApiDto;
import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.model.UserModel;
import com.konomi.authenticationservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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

    @GetMapping("/{userId}")
    public ResponseEntity<?> getById(@PathVariable(value = "userId") UUID userId) {
        UserModel userModel = userService.getById(userId);
        return ResponseEntity.ok().body(userModel);
    }

    @GetMapping
    public ResponseEntity<Page<UserModel>> getAllUsers(@PageableDefault(page = 0, size = 50, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {
        Page<UserModel> pageUsers = userService.findAll(pageable);
        return ResponseEntity.ok().body(pageUsers);
    }

    @PutMapping("/{userId}")
    public ResponseEntity<?> update(@PathVariable(value = "userId") UUID userId,
                                    @RequestBody @JsonView(UserDto.UserView.UserPut.class) UserDto userDto) {
        return userService.update(userId, userDto);
    }

    @PostMapping("/inactive/{userId}")
    public ResponseEntity<?> inactive(@PathVariable(value = "userId") UUID userId) {
        return userService.inactive(userId);
    }
}
