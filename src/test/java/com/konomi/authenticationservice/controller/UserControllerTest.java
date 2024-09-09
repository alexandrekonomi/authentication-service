package com.konomi.authenticationservice.controller;

import com.konomi.authenticationservice.dto.ApiDto;
import com.konomi.authenticationservice.dto.UserDto;
import com.konomi.authenticationservice.model.UserModel;
import com.konomi.authenticationservice.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void whenGetUserByIdThenReturnUser() throws Exception {
        UUID userId = UUID.randomUUID();
        UserModel user = new UserModel();
        user.setId(userId);
        String nameUser = "test name ";
        user.setName(nameUser);

        when(userService.getById(userId)).thenReturn(user);

        mockMvc.perform(get("/api/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(nameUser)));
    }


    @Test
    public void whenSignUpUserThenReturnCreated() throws Exception {
        UserDto userDto = new UserDto();
        userDto.setName("New User");
        userDto.setEmail("newuser@example.com");
        userDto.setPassword("password123");
        userDto.setPasswordConfirmation("password123");
        userDto.setDocument("12345678901");
        userDto.setRoleName("USER");

        ResponseEntity<ApiDto> responseEntity = ResponseEntity.ok(new ApiDto("User account created successfully"));

        Mockito.doReturn(responseEntity).when(userService).signUpUser(Mockito.any(UserDto.class));

        mockMvc.perform(post("/api/user")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"New User\", \"email\": \"newuser@example.com\", \"password\": \"password123\", \"passwordConfirmation\": \"password123\", \"document\": \"12345678901\", \"roleName\": \"USER\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message", is("User account created successfully")));
    }

    @Test
    public void whenGetAllUsersThenReturnUsersPage() throws Exception {

        UserModel user1 = new UserModel();
        user1.setId(UUID.randomUUID());
        user1.setName("John Doe");
        user1.setEmail("john.doe@example.com");

        UserModel user2 = new UserModel();
        user2.setId(UUID.randomUUID());
        user2.setName("Jane Doe");
        user2.setEmail("jane.doe@example.com");

        List<UserModel> users = Arrays.asList(user1, user2);
        Page<UserModel> page = new PageImpl<>(users);

        Mockito.when(userService.findAll(Mockito.any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/user")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.length()").value(2))
                .andExpect(jsonPath("$.content[0].name", is("John Doe")))
                .andExpect(jsonPath("$.content[0].email", is("john.doe@example.com")))
                .andExpect(jsonPath("$.content[1].name", is("Jane Doe")))
                .andExpect(jsonPath("$.content[1].email", is("jane.doe@example.com")))
                .andExpect(jsonPath("$.totalElements").value(2))
                .andExpect(jsonPath("$.size").value(2));
    }

    @Test
    public void whenUpdateUserThenReturnUpdated() throws Exception {
        UUID userId = UUID.randomUUID();

        UserDto userDto = new UserDto();
        userDto.setName("Updated User");
        userDto.setEmail("updated.user@example.com");
        userDto.setDocument("98765432100");
        userDto.setPassword("newpassword123");
        userDto.setPasswordConfirmation("newpassword123");
        userDto.setRoleName("USER");

        UserModel updatedUser = new UserModel();
        updatedUser.setId(userId);
        updatedUser.setName("Updated User");
        updatedUser.setEmail("updated.user@example.com");
        updatedUser.setDocument("98765432100");

        Mockito.doReturn(ResponseEntity.ok(new ApiDto("User updated successfully")))
                .when(userService).update(Mockito.eq(userId), Mockito.any(UserDto.class));


        mockMvc.perform(put("/api/user/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"Updated User\", \"email\": \"updated.user@example.com\", \"document\": \"98765432100\", \"password\": \"newpassword123\", \"passwordConfirmation\": \"newpassword123\", \"roleName\": \"USER\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User updated successfully")));
    }


    @Test
    public void whenInactiveUserThenReturnSuccess() throws Exception {
        UUID userId = UUID.randomUUID();

        UserModel inactiveUser = new UserModel();
        inactiveUser.setId(userId);
        inactiveUser.setName("Inactive User");
        inactiveUser.setEmail("inactive.user@example.com");
        inactiveUser.setDocument("12345678901");
        inactiveUser.setActive(false);

        Mockito.doReturn(new ResponseEntity<>(new ApiDto("User inactivated successfully"), HttpStatus.OK))
                .when(userService).inactive(Mockito.eq(userId));

        mockMvc.perform(post("/api/user/inactive/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message", is("User inactivated successfully")));
    }

}