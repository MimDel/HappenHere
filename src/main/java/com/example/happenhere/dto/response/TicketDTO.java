package com.example.happenhere.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TicketDTO {
    private Long id;
    private Long eventId;
    private LocalDateTime dateBought;
    private boolean refundable;
}
