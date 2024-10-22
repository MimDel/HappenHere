package com.example.happenhere.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

public record MessageResponseDTO(Integer status, String message) implements Serializable {

}
