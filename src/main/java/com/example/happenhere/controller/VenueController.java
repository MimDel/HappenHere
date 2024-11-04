package com.example.happenhere.controller;

import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.dto.request.VenueCreationDTO;
import com.example.happenhere.dto.response.VenueDTO;
import com.example.happenhere.service.VenueService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

@RestController
@RequestMapping("/venue/")
@AllArgsConstructor
public class VenueController {
    public final VenueService venueService;

    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> createVenue(@Valid @RequestBody VenueCreationDTO venueCreationDTO,
                                                          BindingResult bindingResult, Principal principal) {
        if(bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(
                    new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage()));
        }

        venueService.createVenue(venueCreationDTO, principal.getName());
        return ResponseEntity.status(201).body(new MessageResponseDTO(201, "Venue created"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<MessageResponseDTO> deleteVenue(@PathVariable Long id, Principal principal) {
        MessageResponseDTO result = venueService.delete(id, principal);
        return ResponseEntity.status(result.status()).body(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VenueDTO> getVenue(@PathVariable Long id) {
        Optional<VenueDTO> venueDTO = venueService.getVenue(id);
        if(venueDTO.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(venueDTO.get());
    }

    //todo get by principal
}
