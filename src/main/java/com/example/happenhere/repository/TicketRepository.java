package com.example.happenhere.repository;

import com.example.happenhere.model.EventEntity;
import com.example.happenhere.model.TicketEntity;
import com.example.happenhere.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, Integer> {
    Optional<TicketEntity> findByEventAndTicketOwner(EventEntity eventEntity, UserEntity userEntity);
}
