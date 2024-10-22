package com.example.happenhere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
public class OwnerDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
}
