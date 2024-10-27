package com.example.happenhere.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDTO {
    private Long id;
    private Long ownerId;
    private Long eventId;
    private LocalDateTime dateBought;
    private boolean refundable;
}
