package com.example.happenhere.dto.response;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private BigDecimal balance;
}
