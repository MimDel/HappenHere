package com.example.happenhere.repository;

import com.example.happenhere.model.EventEntity;
import com.example.happenhere.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<EventEntity, Long> {

    boolean existsById(Long id);

    Optional<EventEntity> findById(Long aLong);
}
