package com.example.happenhere.service;


import com.example.happenhere.dto.EventCreationDTO;
import com.example.happenhere.dto.EventDTO;
import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.model.*;
import com.example.happenhere.repository.*;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.*;

@Service
public class EventService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final CategoryRepository categoryRepository;

    public EventService(ModelMapper modelMapper,
                        UserRepository userRepository,
                        VenueRepository venueRepository,
                        EventRepository eventRepository,
                        TicketRepository ticketRepository,
                        CategoryRepository categoryRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.categoryRepository = categoryRepository;
    }

    public MessageResponseDTO createEvent(EventCreationDTO eventCreationDTO,
                                          Principal principal) {

        if (eventCreationDTO.getEndingDate().isBefore(eventCreationDTO.getStartingDate())) {
            return new MessageResponseDTO(400, "The ending date is before the starting date");
        }

        UserEntity userEntity = userRepository.findByEmail(principal.getName());
        boolean isOwnerOfVenue = userEntity.getVenues()
                .stream()
                .anyMatch(venue -> venue.getId().equals(eventCreationDTO.getVenueId()));

        Optional<VenueEntity> venueEntity = venueRepository.findById(eventCreationDTO.getVenueId());

        if (venueEntity.isEmpty()) {
            return new MessageResponseDTO(400, "Venue does not exist");
        }

        if (!isOwnerOfVenue) {
            return new MessageResponseDTO(401, "Venue is not owned by the user");
        }

        Set<CategoryEntity> eventCategories = new HashSet<>();

        for(var category: eventCreationDTO.getCategories()) {
            CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);
            categoryRepository.save(categoryEntity);
            eventCategories.add(categoryEntity);
        }

        EventEntity eventEntity = modelMapper.map(eventCreationDTO, EventEntity.class);
        eventEntity.setTickets(new ArrayList<>());
        eventEntity.setVenue(venueEntity.get());
        eventEntity.setCategories(eventCategories);

        eventRepository.save(eventEntity);
        return new MessageResponseDTO(200, "Event created");
    }

    @Transactional
    public MessageResponseDTO delete(Long eventId, Principal principal) {
        UserEntity userEntity = userRepository.findByEmail(principal.getName());
        Optional<EventEntity> event = eventRepository.findById(eventId);

        if (event.isEmpty()) {
            return new MessageResponseDTO(404, "Event not found");
        }

        var venueFromEvent = event.get().getVenue();
        boolean isOwnerOfVenue = userEntity.getVenues()
                .stream()
                .anyMatch(venue -> venue.getId().equals(venueFromEvent.getId()));

        if (!isOwnerOfVenue) {
            return new MessageResponseDTO(401, "Venue is not owned by the user");
        }

        event.get().getTickets()
                .forEach(ticket -> {
                    ticket.getTicketOwner()
                            .getTickets()
                            .remove(ticket);
                    ticketRepository.delete(ticket);
                });

        eventRepository.deleteById(eventId);
        return new MessageResponseDTO(200, "Event deleted");
    }

    public Optional<EventDTO> get(Long id) {
        Optional<EventEntity> eventEntity = eventRepository.findById(id);
        if (eventEntity.isEmpty()) {
            return Optional.empty();
        }
        EventDTO eventDTO = modelMapper.map(eventEntity.get(), EventDTO.class);
        return Optional.of(eventDTO);
    }
}
