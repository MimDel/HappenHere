package com.example.happenhere.service;


import com.example.happenhere.dto.EventCreationDTO;
import com.example.happenhere.dto.MessageResponseDTO;
import com.example.happenhere.model.EventEntity;
import com.example.happenhere.model.TicketEntity;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.repository.EventRepository;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.repository.VenueRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

@Service
public class EventService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;

    public EventService(ModelMapper modelMapper,
                        UserRepository userRepository,
                        VenueRepository venueRepository,
                        EventRepository eventRepository) {
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.venueRepository = venueRepository;
        this.eventRepository = eventRepository;
    }

    public MessageResponseDTO createEvent(EventCreationDTO eventCreationDTO,
                                                    Principal principal) {

        if(eventCreationDTO.getEndingDate().isBefore(eventCreationDTO.getStartingDate())) {
            return new MessageResponseDTO(400, "The ending date is before the starting date");
        }

        UserEntity userEntity = userRepository.findByEmail(principal.getName());
        boolean isOwnerOfVenue = userEntity.getVenues()
                .stream()
                .anyMatch(venue -> venue.getId().equals(eventCreationDTO.getVenueId()));

        Optional<VenueEntity> venueEntity = venueRepository.findById(eventCreationDTO.getVenueId());

        if(venueEntity.isEmpty()) {
            return new MessageResponseDTO(400, "Venue does not exist");
        }

        if(!isOwnerOfVenue) {
            return new MessageResponseDTO(400, "Venue is not owned by the user");
        }

        EventEntity eventEntity = modelMapper.map(eventCreationDTO, EventEntity.class);
        eventEntity.setTickets(new ArrayList<>());
        eventEntity.setVenue(venueEntity.get());

        eventRepository.save(eventEntity);
        return new MessageResponseDTO(200, "Event created");
    }
}
