package com.example.happenhere.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class VenueDTO implements Serializable {
    private Long id;
    private String name;
    private AddressDTO address;
    private String phoneNumber;
    private String description;
    private OwnerDTO owner;
}
