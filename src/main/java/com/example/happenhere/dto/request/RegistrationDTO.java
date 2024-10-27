package com.example.happenhere.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;


import java.io.Serializable;

@Data
public class RegistrationDTO implements Serializable {
    @NotNull(message = "email can't be null")
    @Email(message = "email is invalid")
    private String email;
    @Size(min=8, max = 100)
    private String password;
    @Size(min=3, max = 100, message = "First name should be between 3 - 100 symbols.")
    private String firstName;
    @Size(min=3, max = 100, message = "Last name should be between 3 - 100 symbols.")
    private String lastName;
}
