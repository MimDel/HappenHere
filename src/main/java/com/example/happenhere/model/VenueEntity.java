package com.example.happenhere.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@Entity
@Table(name = "venues")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class VenueEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String email;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private AddressEntity address;

    @Column(nullable = false, unique = true)
    private String phoneNumber;

    private String type;

    @Column(length = 10000)
    private String description;

    @ManyToOne
    private UserEntity owner;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.ALL)
    private List<EventEntity> events;
}
