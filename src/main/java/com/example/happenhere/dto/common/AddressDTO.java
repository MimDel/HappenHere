package com.example.happenhere.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class AddressDTO implements Serializable {

    @NotNull
    @NotBlank
    private String addressLine1;

    @NotBlank
    private String addressLine2;

    @NotNull
    private String town;

    @NotNull
    private String country;

    @NotNull
    @NotBlank
    private String postcode;
}
