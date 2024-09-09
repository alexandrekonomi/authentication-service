package com.konomi.authenticationservice.service.impl;

import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.enums.RoleType;
import com.konomi.authenticationservice.exception.BadRequestException;
import com.konomi.authenticationservice.exception.ConflictException;
import com.konomi.authenticationservice.exception.ResourceNotFoundException;
import com.konomi.authenticationservice.model.RoleModel;
import com.konomi.authenticationservice.model.UserModel;
import com.konomi.authenticationservice.repository.RoleRepository;
import com.konomi.authenticationservice.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void whenSignupUserThenUserIsCreated() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test.user@example.com");
        userDto.setPassword("password123");
        userDto.setPasswordConfirmation("password123");
        userDto.setDocument("12345678901");
        userDto.setRoleName("ROLE_CUSTOMER");

        RoleModel role = new RoleModel();

        Mockito.when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(false);
        Mockito.when(userRepository.existsByDocument(userDto.getDocument())).thenReturn(false);
        Mockito.when(roleRepository.findByRoleName(RoleType.ROLE_CUSTOMER)).thenReturn(Optional.of(role));

        ResponseEntity<?> response = userService.signUpUser(userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        Mockito.verify(userRepository).save(userCaptor.capture());
        UserModel savedUser = userCaptor.getValue();

        assertEquals("Test User", savedUser.getName());
        assertEquals("test.user@example.com", savedUser.getEmail());
        assertEquals("12345678901", savedUser.getDocument());
        assertTrue(savedUser.getRoles().contains(role));
    }

    @Test
    public void whenSignUpUserThenPasswordNotMatch() {
        UserDto userDto = new UserDto();
        userDto.setPassword("password123");
        userDto.setPasswordConfirmation("password467");

        Exception exception = assertThrows(BadRequestException.class, () -> {
            userService.signUpUser(userDto);
        });

        assertEquals("Passwords do not match", exception.getMessage());
    }

    @Test
    public void whenSignUpUserThenEmailAlreadyRegistered() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test.user@example.com");
        userDto.setPassword("password123");
        userDto.setPasswordConfirmation("password123");
        userDto.setDocument("12345678901");
        userDto.setRoleName("ROLE_CUSTOMER");


        Mockito.when(userRepository.existsByEmail(userDto.getEmail())).thenReturn(true);

        Exception exception = assertThrows(ConflictException.class, () -> {
            userService.signUpUser(userDto);
        });

        assertEquals("Email is already registered", exception.getMessage());
    }

    @Test
    public void whenSignUpThenDocumentAlreadyRegistered() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test.user@example.com");
        userDto.setPassword("password123");
        userDto.setPasswordConfirmation("password123");
        userDto.setDocument("12345678901");
        userDto.setRoleName("ROLE_CUSTOMER");

        Mockito.when(userRepository.existsByDocument(userDto.getDocument())).thenReturn(true);

        Exception exception = assertThrows(ConflictException.class, () -> {
            userService.signUpUser(userDto);
        });

        assertEquals("Document is already registered", exception.getMessage());
    }

    @Test
    public void whenSigUpUserThenRoleNotFound() {
        UserDto userDto = new UserDto();
        userDto.setName("Test User");
        userDto.setEmail("test.user@example.com");
        userDto.setPassword("password123");
        userDto.setPasswordConfirmation("password123");
        userDto.setDocument("12345678901");
        userDto.setRoleName("ROLE_CUSTOMER");

        Mockito.when(roleRepository.findByRoleName(RoleType.ROLE_CUSTOMER))
                .thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.signUpUser(userDto);
        });

        assertEquals("role not found with roleName : ROLE_CUSTOMER", exception.getMessage());
    }

    @Test
    public void whenGetByIdThenReturnUser() {

        UUID userId = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setId(userId);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserModel foundUser = userService.getById(userId);

        assertEquals(userId, foundUser.getId());
    }

    @Test
    public void whenGetByIdThenThrowResourceNotFoundException() {
        UUID userId = UUID.randomUUID();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.getById(userId);
        });

        assertEquals("user not found with id : " + userId, exception.getMessage());
    }

    @Test
    public void whenFindAllThenReturnPageOfUsers() {
        Pageable pageable = PageRequest.of(0,10);
        UserModel user = new UserModel();
        user.setName("Test User");

        Page<UserModel> page = new PageImpl<>(Collections.singletonList(user));

        Mockito.when(userRepository.findAll(pageable)).thenReturn(page);

        Page<UserModel> result = userService.findAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test User", result.getContent().get(0).getName());
    }

    @Test
    public void whenInactive_thenUserIsInactive() {
        UUID userId = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setId(userId);
        user.setActive(true);

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.inactive(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertFalse(user.getActive());
    }

    @Test
    public void whenInactive_thenThrowResourceNotFoundException() {
        UUID userId = UUID.randomUUID();

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            userService.inactive(userId);
        });

        assertEquals("user not found with id : " + userId, exception.getMessage());
    }

    @Test
    public void whenUpdateThenUserIsUpdated() {
        UUID userId = UUID.randomUUID();
        UserDto userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated.user@example.com");
        userDto.setDocument("98765432100");

        UserModel user = new UserModel();
        user.setId(userId);
        user.setName("Old Name");
        user.setEmail("old.email@example.com");
        user.setDocument("12345678901");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        ResponseEntity<?> response = userService.update(userId, userDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated User", user.getName());
        assertEquals("updated.user@example.com", user.getEmail());
        assertEquals("98765432100", user.getDocument());
    }

}