package com.example.happenhere.controller;

import com.example.happenhere.dto.common.FilterDTO;
import com.example.happenhere.dto.enums.SortEnum;
import com.example.happenhere.dto.request.EventCreationDTO;
import com.example.happenhere.dto.response.EventDTO;
import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;


    @PostMapping("/create")
    public ResponseEntity<MessageResponseDTO> createEvent(@Valid @RequestBody EventCreationDTO eventCreationDTO,
                                                          BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(new MessageResponseDTO(400, bindingResult.getAllErrors().get(0).getDefaultMessage()));
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
        if (event.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(event.get());
    }

    @GetMapping
    public ResponseEntity<List<EventDTO>> getEvents(
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) LocalDateTime dateFrom,
            @RequestParam(required = false) LocalDateTime dateTo,
            @RequestParam(required = false) BigDecimal priceFrom,
            @RequestParam(required = false) BigDecimal priceTo,
            @RequestParam(required = false) String sortBy,
            Pageable pageable
    ) {
        FilterDTO filterDTO = new FilterDTO(city, country, category != null ? category.split(",") : null, dateFrom, dateTo, priceFrom, priceTo);
        return ResponseEntity.ok(eventService.getEvents(
                pageable, filterDTO, sortBy
        ));
    }
}
