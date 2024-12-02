package com.example.happenhere.dto.request;

import com.example.happenhere.dto.common.CategoryDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventCreationDTO implements Serializable {

    @NotNull(message = "The name of the event can't be null")
    private String name;

    private String description;

    @NotNull
    @Positive(message = "The price of the event should be positive.")
    private BigDecimal price;

    @Positive(message = "The maximum quantity of tickets should be positive.")
    private Integer maxNumberOfTickets;
    @NotNull
    private boolean refundable= true;

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
