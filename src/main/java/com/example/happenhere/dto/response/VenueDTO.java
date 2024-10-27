package com.example.happenhere.dto.response;

import com.example.happenhere.dto.common.AddressDTO;
import com.example.happenhere.dto.common.OwnerDTO;
import lombok.Data;

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
