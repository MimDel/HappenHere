package com.example.happenhere.dto.request;

import com.example.happenhere.dto.common.CategoryDTO;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class EventCreationDTO implements Serializable {

    @NotNull(message = "The name of the event can't be null")
    private String name;

    private String description;

    @NotNull
    private BigDecimal price;

    private Integer maxQuantity;

    @NotNull
    private Long venueId;

    @NotNull
    @Future(message = "The starting date of the event should be after the current date.")
    private LocalDateTime startingDate;

    @NotNull
    @Future(message = "The starting date of the event should be after the current date.")
    private LocalDateTime endingDate;

    private Set<CategoryDTO> categories;

}
