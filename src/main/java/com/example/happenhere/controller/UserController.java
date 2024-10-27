package com.example.happenhere.controller;

import com.example.happenhere.dto.response.JWTDTO;
import com.example.happenhere.dto.response.LoginDTO;
import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.dto.request.RegistrationDTO;
import com.example.happenhere.service.UserService;
import com.example.happenhere.utils.JWTUtils;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/user/")
@Slf4j
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @PostMapping("register")
    public ResponseEntity<MessageResponseDTO> register(@Valid @RequestBody RegistrationDTO registrationDTO,
                                                       BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(400,bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }
        Optional<RegistrationDTO> optionalRegistrationDTO = userService.registerUser(registrationDTO);

        if (optionalRegistrationDTO.isEmpty()) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(400,"User registration failed"));
        }

        return ResponseEntity.ok(new MessageResponseDTO(200,"User registration successful"));
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

}
