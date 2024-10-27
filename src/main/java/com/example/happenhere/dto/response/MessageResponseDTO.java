package com.example.happenhere.dto.response;

import java.io.Serializable;

public record MessageResponseDTO(Integer status, String message) implements Serializable {

}
