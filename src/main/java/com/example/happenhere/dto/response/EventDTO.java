package com.example.happenhere.dto.response;

import com.example.happenhere.dto.common.CategoryDTO;
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
    private Integer maxNumberOfTickets;
    private LocalDateTime startingTime;
    private LocalDateTime endingTime;
    private Long venueId;
    private List<CategoryDTO> categories;
}
