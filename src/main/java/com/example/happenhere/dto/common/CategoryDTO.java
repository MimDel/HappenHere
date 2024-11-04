package com.example.happenhere.dto.common;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class CategoryDTO implements Serializable {
    private String name;
}
