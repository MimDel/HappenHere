package com.example.happenhere.controller;

import com.example.happenhere.dto.JWTDTO;
import com.example.happenhere.dto.LoginDTO;
import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.dto.RegistrationDTO;
import com.example.happenhere.service.UserService;
import com.example.happenhere.utils.JWTUtils;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/")
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    public UserController(UserService userService, AuthenticationManager authenticationManager, JWTUtils jwtUtils){
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("register")
    public ResponseEntity<MessageResponseDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO,
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

    @PostMapping("login")
    public ResponseEntity<JWTDTO> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().build();
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.email(), loginDTO.password())
        );
        UserDetails userDetails =(UserDetails) authentication.getPrincipal();
        String jwt = jwtUtils.generateToken(userDetails.getUsername());
        return ResponseEntity.ok(new JWTDTO(userDetails.getUsername(), jwt));
    }

    @GetMapping("/test")
    public ResponseEntity<Void> test() {
        return ResponseEntity.ok().build();
    }
}
