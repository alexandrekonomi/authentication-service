package com.konomi.authenticationservice.service;

import com.konomi.authenticationservice.dto.ApiDto;
import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.enums.RoleType;
import com.konomi.authenticationservice.exception.BadRequestException;
import com.konomi.authenticationservice.exception.ConflictException;
import com.konomi.authenticationservice.exception.ResourceNotFoundException;
import com.konomi.authenticationservice.model.RoleModel;
import com.konomi.authenticationservice.model.UserModel;
import com.konomi.authenticationservice.repository.RoleRepository;
import com.konomi.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @Override
    public ResponseEntity<?> signUpUser(UserDto userDto) {

        String password = userDto.getPassword();
        String passwordConfirmation = userDto.getPasswordConfirmation();

        if (!password.equals(passwordConfirmation)) {
            throw new BadRequestException("Passwords do not match");
        }

        String email = userDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            log.warn("Email {} is already registered", email);
            throw new ConflictException("Email is already registered");
        }

        String document = userDto.getDocument();
        if (userRepository.existsByDocument(document)) {
            log.warn("Document {} is already registered", document);
            throw new ConflictException("Document is already registered");
        }

        RoleType roleType = RoleType.valueOf(userDto.getRoleName());

        RoleModel role = roleRepository.findByRoleName(roleType)
                .orElseThrow(() -> new ResourceNotFoundException("roel", "roleName", userDto.getRoleName()));

        UserModel user = new UserModel();
        user.setName(userDto.getName());
        user.setEmail(email);
        user.setDocument(document);
        user.setPassword(password);
        user.setActive(true);
        user.getRoles().add(role);

        userRepository.save(user);

        log.debug("User registered successfully {} ", user.getId());
        log.info("User registered successfully {} ", user.getId());
        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @Override
    public UserModel getById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("user", "id", userId));
    }

    @Override
    public Page<UserModel> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public ResponseEntity<?> inactive(UUID userId) {
        UserModel userModel = getById(userId);
        userModel.setActive(false);

        log.debug("User inactivated successfully {}", userModel.getId());
        log.info("User inactivated successfully {}", userModel.getId());
        return new ResponseEntity<>(new ApiDto("User inactivated successfully"), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> update(UUID userId, UserDto userDto) {
        UserModel userModel = getById(userId);

        String name = StringUtils.stripToNull(userDto.getName());
        if (Objects.nonNull(name)) {
            userModel.setName(name);
        }

        String document = StringUtils.stripToNull(userDto.getDocument());
        if (Objects.nonNull(document)) {
            userModel.setName(document);
        }

        String email = StringUtils.stripToNull(userDto.getEmail());
        if (Objects.nonNull(email)) {
            userModel.setName(email);
        }

        userModel.setLastUpdateDate(LocalDateTime.now(ZoneId.of("UTC")));
        userRepository.save(userModel);

        log.debug("User updated successfully {} ", userModel.getId());
        log.info("User updated successfully {} ", userModel.getId());

        return new ResponseEntity<>(new ApiDto("User updated successfully"), HttpStatus.OK);
    }


}
