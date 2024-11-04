package com.example.happenhere.controller;

import com.example.happenhere.dto.request.RegistrationDTO;
import com.example.happenhere.dto.response.LoginDTO;
import com.example.happenhere.init.DbInit;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.service.UserService;
import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    UserService userService;
    @MockBean
    DbInit dbInit;

    private final Gson gson = new Gson();

    @Test
    void registerSuccess() throws Exception {
        String email = "user@gmail.com";
        String password = "password@gmail.com";
        RegistrationDTO registrationDTO = new RegistrationDTO(email, password, "Test", "Testov");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(registrationDTO)))
                .andExpect(status().isOk());
        Assertions.assertEquals(userRepository.count(), 1);
        Optional<UserEntity> user = userRepository.findByEmail(email);
        if (user.isEmpty())
            Assertions.fail("Cannot find user with email just registered");
        Assertions.assertEquals(user.get().getEmail(), email);
        Assertions.assertTrue(passwordEncoder.matches(password, user.get().getPassword()));
    }

    @Test
    void registerFail() throws Exception {
        userService.seedUsers();
        RegistrationDTO registrationDTO = new RegistrationDTO("user@gmail.com", "password", "user", "userov");

        mockMvc.perform(post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(registrationDTO)))
                .andExpect(status().isBadRequest());
        registrationDTO.setEmail("invalidahhmail");
        mockMvc.perform(post("http://localhost.com/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(registrationDTO)))
                .andExpect(status().isBadRequest());
        Assertions.assertEquals(userRepository.count(), 1);
    }

    @Test
    void loginSuccess() throws Exception {
        userService.seedUsers();
        LoginDTO loginDTO = new LoginDTO("user@gmail.com", "password");

        mockMvc.perform(post("http://localhost.com/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginDTO))
        ).andExpect(status().isOk());
    }

    @Test
    void loginFail() throws Exception {
        userService.seedUsers();

        LoginDTO loginDTO = new LoginDTO("user@gmail.com", "password1");
        mockMvc.perform(post("http://localhost.com/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginDTO)))
                .andExpect(status().isForbidden());

        loginDTO = new LoginDTO("invalid", "password");
        mockMvc.perform(post("http://localhost.com/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginDTO)))
                .andExpect(status().isForbidden());

        loginDTO = new LoginDTO("user@gmail.com", "");
        mockMvc.perform(post("http://localhost.com/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(loginDTO)))
                .andExpect(status().isForbidden());
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }
}
