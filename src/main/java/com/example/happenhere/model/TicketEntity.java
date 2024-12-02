package com.example.happenhere.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "tickets")
@Data
public class TicketEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    private EventEntity event;

    @ManyToOne
    private UserEntity ticketOwner;

    private LocalDateTime dateBought;
}