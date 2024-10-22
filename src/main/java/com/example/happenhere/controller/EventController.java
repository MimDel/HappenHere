package com.example.happenhere.controller;

import com.example.happenhere.dto.EventCreationDTO;
import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.service.EventService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        MessageResponseDTO resultMessage = eventService.createEvent(eventCreationDTO, principal);

        if(resultMessage.status().equals(400)){
            return ResponseEntity.badRequest().body(resultMessage);
        }

        return ResponseEntity.ok(resultMessage);
    }

}
