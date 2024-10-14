package com.example.happenhere.controller;

import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.dto.RegistrationDTO;
import com.example.happenhere.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/user/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @PostMapping("register")
    public ResponseEntity<MessageResponseDTO> register(@Valid RegistrationDTO registrationDTO,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }
        Optional<RegistrationDTO> optionalRegistrationDTO = userService.registerUser(registrationDTO);

        if (optionalRegistrationDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO("User registration failed"));
        }

        return ResponseEntity.ok(new MessageResponseDTO("User registration successful"));
    }
}
