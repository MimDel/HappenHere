package com.example.happenhere.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Entity
@Table(name = "addresses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotNull
    private String addressLine1;

    private String addressLine2;

    @Column(nullable = false)
    private String town;

    @Column(nullable = false)
    private String country;

    @Column(nullable = false)
    private String postcode;

    @OneToOne(mappedBy = "address")
    private VenueEntity venue;
}