package com.example.happenhere.repository;

import com.example.happenhere.model.VenueEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VenueRepository extends JpaRepository<VenueEntity, Long> {
    boolean existsById(Long id);
}
