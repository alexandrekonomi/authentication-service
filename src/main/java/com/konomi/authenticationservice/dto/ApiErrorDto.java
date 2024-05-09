package com.konomi.authenticationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.util.List;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiErrorDto {
    private HttpStatus status;
    private String code;
    private List<String> errors;

    public ApiErrorDto(HttpStatus status, String code, List<String> errors) {
        this.status = status;
        this.code = code;
        this.errors = errors;
    }
}
