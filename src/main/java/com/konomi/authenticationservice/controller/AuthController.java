package com.konomi.authenticationservice.controller;

import com.konomi.authenticationservice.dto.JwtResponseDto;
import com.konomi.authenticationservice.dto.LoginRequestDto;
import com.konomi.authenticationservice.model.UserModel;
import com.konomi.authenticationservice.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto loginRequestDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getEmail(),
                        loginRequestDto.getPassword()
                )
        );

        UserModel user = (UserModel) authentication.getPrincipal();

        String refreshToken = jwtService.generateRefreshToken(user);
        Date expiresAt = jwtService.getTokenExpiration();

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new JwtResponseDto(
                token,
                refreshToken,
                "Bearer",
                expiresAt,
                user.getRoles().stream().findFirst().map(role -> role.getRoleName().name()).orElse("UNKNOWN")
        ));
    }
}
