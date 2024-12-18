package com.example.happenhere.controller;

import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.dto.response.TicketDTO;
import com.example.happenhere.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.aspectj.bridge.Message;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {
    private  final TicketService ticketService;

    @PostMapping("/buy/{eventId}")
    public ResponseEntity<MessageResponseDTO> buyTicket(@PathVariable Long eventId, Principal principal) {
        MessageResponseDTO res = ticketService.buyTicket(eventId, principal.getName());
        return ResponseEntity.status(res.status()).body(res);
    }

    @PostMapping("/refund/{eventId}")
    public ResponseEntity<MessageResponseDTO> refundTicket(@PathVariable Long eventId, Principal principal) {
        MessageResponseDTO res = ticketService.refundTicket(eventId, principal.getName());
        return ResponseEntity.status(res.status()).body(res);
    }

    @GetMapping("/myTickets")
    public ResponseEntity<List<TicketDTO>> myTickets(Principal principal) {
        List<TicketDTO> tickets = ticketService.getTickets(principal.getName());
        return ResponseEntity.ok(tickets);
    }
}
