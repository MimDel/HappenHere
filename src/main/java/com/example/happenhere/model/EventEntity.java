package com.example.happenhere.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "events")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 10000)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private Integer maxNumberOfTickets;

    @OneToMany(mappedBy = "event")
    private List<TicketEntity> tickets;
    @Column(nullable = false)
    private boolean refundable;

    @Column(nullable = false)
    private LocalDateTime startingDate;
    @Column()
    private LocalDateTime endingDate;

    @ManyToOne
    private VenueEntity venue;

    @ManyToMany
    @JoinTable(name = "events_categories",
    joinColumns = @JoinColumn(name = "events_id"),
    inverseJoinColumns = @JoinColumn(name = "categories_id"))
    private List<CategoryEntity> categories;
}
