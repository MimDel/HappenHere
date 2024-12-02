package com.example.happenhere.service;

import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.model.EventEntity;
import com.example.happenhere.model.TicketEntity;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.repository.EventRepository;
import com.example.happenhere.repository.TicketRepository;
import com.example.happenhere.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    @Transactional
    public MessageResponseDTO buyTicket(Long eventId, String name) {
        Optional<UserEntity> user = userRepository.findByEmail(name);
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if (user.isEmpty()) {
            return new MessageResponseDTO(404, "User not found");
        }
        if (event.isEmpty()) {
            return new MessageResponseDTO(404, "Event not found");
        }
        if (user.get().getBalance().compareTo(event.get().getPrice()) < 0) {
            return new MessageResponseDTO(400, "Not enough money");
        }
        if (event.get().getTickets().size() < event.get().getMaxNumberOfTickets()) {
            return new MessageResponseDTO(400, "No more tickets available");
        }
        TicketEntity ticket = new TicketEntity();
        ticket.setEvent(event.get());
        ticket.setTicketOwner(user.get());
        ticket.setRefundable(event.get().isRefundable());
        ticket.setDateBought(LocalDateTime.now());

        ticketRepository.save(ticket);
        event.get().getTickets().add(ticket);
        user.get().getTickets().add(ticket);
        user.get().setBalance(user.get().getBalance().subtract(event.get().getPrice()));

        userRepository.save(user.get());
        eventRepository.save(event.get());

        return new MessageResponseDTO(200, "Ticket bought successfully");
    }

    public MessageResponseDTO refundTicket(Long eventId, String name) {
        Optional<UserEntity> user = userRepository.findByEmail(name);
        Optional<EventEntity> event = eventRepository.findById(eventId);
        if (user.isEmpty()) {
            return new MessageResponseDTO(404, "User not found");
        }
        if (event.isEmpty()) {
            return new MessageResponseDTO(404, "Event not found");
        }
        Optional<TicketEntity> ticket = ticketRepository.findByEventAndTicketOwner(event.get(), user.get());
        if (ticket.isEmpty()) {
            return new MessageResponseDTO(404, "Ticket not found");
        }
        if (!ticket.get().isRefundable()) {
            return new MessageResponseDTO(400, "Ticket is not refundable");
        }
        user.get().setBalance(user.get().getBalance().add(event.get().getPrice()));
        user.get().getTickets().remove(ticket.get());
        event.get().getTickets().remove(ticket.get());
        ticketRepository.delete(ticket.get());
        userRepository.save(user.get());
        eventRepository.save(event.get());
        return new MessageResponseDTO(200, "Ticket refunded successfully");
    }
}
