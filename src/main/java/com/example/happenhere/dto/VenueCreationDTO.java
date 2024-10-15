package com.example.happenhere.dto;

import com.example.happenhere.model.UserEntity;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class VenueCreationDTO implements Serializable {

    @NotNull(message = "The name of the venue cat'n be null")
    private String name;

    @NotNull(message = "The email can't be null")
    @Email(message = "email is invalid")
    private String email;

    //todo add address class
    @NotNull
    @NotBlank(message = "You need to specify the venue address")
    private String address;

    @NotNull
    @NotBlank(message = "You need to specify the venue phoneNumber")
    private String phoneNumber;

    private String description;

    @Size(max = 100)
    @NotBlank(message = "You need to specify the venue type")
    private String type;

    private UserEntity owner;

}
