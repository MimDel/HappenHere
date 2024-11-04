package com.example.happenhere.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO implements Serializable {

    @NotNull
    @NotBlank
    private String addressLine1;

    private String addressLine2;

    @NotNull
    private String town;

    @NotNull
    private String country;

    @NotNull
    @NotBlank
    private String postcode;
}
