package com.example.happenhere.dto.common;

import lombok.Data;

import java.io.Serializable;

@Data
public class OwnerDTO implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
}
