package com.konomi.authenticationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiDto {
    private boolean success = true;
    private Object data;
    private String message;

    public ApiDto(Object data, String message) {
        this.data = data;
        this.message = message;
    }

    public ApiDto(String message) {
        this.message = message;
    }

    public ApiDto(Object data) {
        this.data = data;
    }
}
