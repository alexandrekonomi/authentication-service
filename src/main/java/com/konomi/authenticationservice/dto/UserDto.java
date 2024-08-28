package com.konomi.authenticationservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonView;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDto {


    public interface UserView {
        interface SignupUser {
        }

        interface SignupAdmin {
        }

        public class UserPut {
        }
    }

    @NotEmpty(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @NotNull(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @JsonView({UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class, UserDto.UserView.UserPut.class})
    private String name;
    @NotEmpty(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @NotNull(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @Size(min = 11, max = 14, groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class, UserDto.UserView.UserPut.class})
    @JsonView({UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    private String document;
    @NotEmpty(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @NotNull(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @Email(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class, UserDto.UserView.UserPut.class})
    @JsonView({UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class, UserDto.UserView.UserPut.class})
    private String email;
    @NotEmpty(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @NotNull(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @Size(min = 6, max = 20, groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @JsonView({UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    private String password;
    @NotEmpty(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @NotNull(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @Size(min = 6, max = 20, groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @JsonView({UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    private String passwordConfirmation;
    @NotEmpty(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @NotNull(groups = {UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    @JsonView({UserDto.UserView.SignupUser.class, UserDto.UserView.SignupAdmin.class})
    public String roleName;

}
