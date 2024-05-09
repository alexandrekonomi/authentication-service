package com.konomi.authenticationservice.service;

import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.enums.RoleType;
import com.konomi.authenticationservice.model.RoleModel;
import com.konomi.authenticationservice.model.UserModel;
import com.konomi.authenticationservice.repository.RoleRepository;
import com.konomi.authenticationservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    @SneakyThrows //adicionar um global exp handler e remover essa anotation
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
            throw new BadRequestException("Email is already registered"); //TODO criar uma exp especifica
        }

        String document = userDto.getDocument();
        if (userRepository.existsByDocument(document)) {
            log.warn("Document {} is already registered", document);
            throw new BadRequestException("Document is already registered"); //TODO criar uma exp especifica
        }

        RoleType roleType = RoleType.valueOf(userDto.getRoleName());

        RoleModel role = roleRepository.findByRoleName(roleType)
                .orElseThrow(() -> new BadRequestException("Erro")); //TODO criar uma exp especifica

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
}
