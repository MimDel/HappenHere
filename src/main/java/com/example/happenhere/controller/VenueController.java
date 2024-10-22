package com.example.happenhere.controller;

import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.dto.VenueCreationDTO;
import com.example.happenhere.dto.VenueDTO;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.service.VenueService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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
                    new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        venueService.createVenue(venueCreationDTO, principal.getName());
        return ResponseEntity.ok(new MessageResponseDTO(200, "Venue created"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteVenue(@PathVariable Long id) {
         boolean isDeleted = venueService.delete(id);
         if(!isDeleted) {
             return ResponseEntity.notFound().build();
         }
         return ResponseEntity.ok(new MessageResponseDTO(200,"Venue deleted"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenue(@PathVariable Long id) {
        Optional<VenueDTO> venueDTO = venueService.getVenue(id);
        if(venueDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venueDTO.get());
    }
}
