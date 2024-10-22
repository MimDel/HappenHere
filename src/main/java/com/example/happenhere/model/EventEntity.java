package com.example.happenhere.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "events")
@Data
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column
    private Integer maxQuantity;

    @OneToMany(mappedBy = "event")
    private List<TicketEntity> tickets;

    @Column(nullable = false)
    private LocalDateTime startingDate;
    @Column(nullable = false)
    private LocalDateTime endingDate;

    @ManyToOne
    private VenueEntity venue;

    @ManyToMany
    @JoinTable(name = "events_categories",
    joinColumns = @JoinColumn(name = "events_id"),
    inverseJoinColumns = @JoinColumn(name = "categories_id"))
    private Set<CategoryEntity> categories;
}
