package com.example.happenhere.controller;

import com.example.happenhere.dto.response.JWTDTO;
import com.example.happenhere.dto.request.LoginDTO;
import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.dto.request.RegistrationDTO;
import com.example.happenhere.dto.response.UserDTO;
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

import java.math.BigDecimal;
import java.security.Principal;
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

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
         Optional<UserDTO> user = userService.getUser(id);
         return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("deposit")
    public ResponseEntity<MessageResponseDTO> deposit(@RequestParam BigDecimal amount, Principal principal) {
        MessageResponseDTO result = userService.deposit(amount, principal);
        return ResponseEntity.status(result.status()).body(result);
    }

    @PostMapping("withdraw")
    public ResponseEntity<MessageResponseDTO> withdraw(@RequestParam BigDecimal amount, Principal principal) {
       MessageResponseDTO result = userService.withdraw(amount,principal);
       return ResponseEntity.status(result.status()).body(result);
    }

}
