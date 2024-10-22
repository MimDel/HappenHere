package com.example.happenhere.controller;

import com.example.happenhere.dto.EventCreationDTO;
import com.example.happenhere.dto.EventDTO;
import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/event/")
public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> createEvent(@Valid @RequestBody EventCreationDTO eventCreationDTO,
                                                          BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(400,bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        MessageResponseDTO result = eventService.createEvent(eventCreationDTO, principal);
        return ResponseEntity.status(result.status()).body(result);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteEvent(@PathVariable Long id, Principal principal) {
         MessageResponseDTO result = eventService.delete(id, principal);
         return ResponseEntity.status(result.status()).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EventDTO> getEvent(@PathVariable Long id) {
        var event = eventService.get(id);
        if(event.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(event.get());
    }
}
