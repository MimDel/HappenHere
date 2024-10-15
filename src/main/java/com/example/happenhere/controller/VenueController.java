package com.example.happenhere.controller;

import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.dto.VenueCreationDTO;
import com.example.happenhere.service.VenueService;
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
@RequestMapping("/venue/")
public class VenueController {
    public final VenueService venueService;

    public VenueController(VenueService venueService) {
        this.venueService = venueService;
    }

    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> createVenue(@Valid @RequestBody VenueCreationDTO venueCreationDTO,
                                                          BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        venueService.createVenue(venueCreationDTO, principal.getName());
        return ResponseEntity.ok(new MessageResponseDTO("Venue created"));
    }
}
