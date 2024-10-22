package com.example.happenhere.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class EventDTO implements Serializable {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private Integer maxQuantity;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private Long venueId;
    private List<CategoryDTO> categories;
}
