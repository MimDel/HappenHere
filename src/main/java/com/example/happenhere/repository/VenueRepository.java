package com.example.happenhere.repository;

import com.example.happenhere.model.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
    boolean existsById(Long id);
    Optional<VenueEntity> findById(Long id);
    Optional<VenueEntity> findByPhoneNumber(String phoneNumber);
}
