package com.example.happenhere.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.io.Serializable;

public record RegistrationDTO(
    @NotNull(message = "email can't be null")
    @Email(message = "email is invalid")
    String email,
    @Size(min=8, max = 100)
    String password,
    @Size(min=3, max = 100, message = "First name should be between 3 - 100 symbols.")
    String firstName,
    @Size(min=3, max = 100, message = "Last name should be between 3 - 100 symbols.")
    String lastName
) implements Serializable {}
