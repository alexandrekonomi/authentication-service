package com.konomi.authenticationservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class JwtResponseDto {
    private String token;
    private String refreshToken;
    private String tokenType;
    private Date expiresAt;
    private String roleType;
}
