package com.example.happenhere.dto.request;

import com.example.happenhere.dto.common.AddressDTO;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.io.Serializable;

@Data
public class VenueCreationDTO implements Serializable {

    @NotNull(message = "The name of the venue cat'n be null")
    private String name;

    @NotNull(message = "The email can't be null")
    @Email(message = "email is invalid")
    private String email;

    @NotNull
    private AddressDTO address;

    @NotNull
    @NotBlank(message = "You need to specify the venue phoneNumber")
    private String phoneNumber;

    private String description;

    @Size(max = 100)
    @NotBlank(message = "You need to specify the venue type")
    private String type;
}
