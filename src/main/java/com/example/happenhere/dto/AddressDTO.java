package com.example.happenhere.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO implements Serializable {

    @NotNull
    @NotBlank
    private String addressLine1;

    @NotBlank
    private String addressLine2;

    @NotNull
    private String town;

    @NotNull
    private String county;

    @NotNull
    @Size(min = 4, max = 4, message = "The postcode should be 4 digits.")
    private String postcode;
}
