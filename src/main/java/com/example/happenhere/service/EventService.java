package com.example.happenhere.service;


import com.example.happenhere.dto.common.FilterDTO;
import com.example.happenhere.dto.enums.SortEnum;
import com.example.happenhere.dto.request.EventCreationDTO;
import com.example.happenhere.dto.response.EventDTO;
import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.model.*;
import com.example.happenhere.repository.*;
import com.example.happenhere.utils.LogMessages;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
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

        UserEntity userEntity = userRepository.findByEmail(principal.getName()).orElseThrow(RuntimeException::new);
        boolean isOwnerOfVenue = userEntity.getVenues()
                .stream()
                .anyMatch(venue -> venue.getId().equals(eventCreationDTO.getVenueId()));

        Optional<VenueEntity> venueEntity = venueRepository.findById(eventCreationDTO.getVenueId());

        if (venueEntity.isEmpty()) {
            return new MessageResponseDTO(400, "Venue does not exist");
        }

        if (!isOwnerOfVenue) {
            return new MessageResponseDTO(403, "Venue is not owned by the user");
        }

        List<CategoryEntity> eventCategories = new ArrayList<>();

        //todo do the logic if the category has been already saved in the database
        for (var category : eventCreationDTO.getCategories()) {
            CategoryEntity categoryEntity = modelMapper.map(category, CategoryEntity.class);
            categoryRepository.save(categoryEntity);
            eventCategories.add(categoryEntity);
        }

        EventEntity eventEntity = modelMapper.map(eventCreationDTO, EventEntity.class);
        eventEntity.setTickets(new ArrayList<>());
        eventEntity.setVenue(venueEntity.get());
        eventEntity.setCategories(eventCategories);

        eventRepository.save(eventEntity);
        return new MessageResponseDTO(201, "Event created");
    }

    @Transactional
    public MessageResponseDTO delete(Long eventId, Principal principal) {
        UserEntity userEntity = userRepository.findByEmail(principal.getName()).orElseThrow(RuntimeException::new);
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

    @Transactional
    public void seedEvents() {
        if (eventRepository.count() == 0) {
            log.info(LogMessages.DB_INITIALIZING, "events");

            VenueEntity monkeyHouse = venueRepository.findByPhoneNumber("8888888888").orElseThrow(RuntimeException::new);
            VenueEntity champlainExpo = venueRepository.findByPhoneNumber("9999999999").orElseThrow(RuntimeException::new);

            CategoryEntity liveMusicCategory = new CategoryEntity("Live Music");
            CategoryEntity openMicCategory = new CategoryEntity("Open Mic");
            CategoryEntity demolitionDerbyCategory = new CategoryEntity("Demolition Derby");
            CategoryEntity outdoorsCategory = new CategoryEntity("Outdoors");

            categoryRepository.save(liveMusicCategory);
            categoryRepository.save(openMicCategory);
            categoryRepository.save(demolitionDerbyCategory);
            categoryRepository.save(outdoorsCategory);


            EventEntity openMicMusic = new EventEntity();
            openMicMusic.setName("Music Open Mic");
            openMicMusic.setDescription("""
                    Music Open Mic at The Monkey House on the last Tuesday of every month! from 7:00pm to 9:00pm hosted by Mike Paternoster. Free!
                    There will be no backline. Please bring all the instruments needed to perform.
                    Each performer gets 10ish minute sets depending on the number of performers.
                    
                    Come hang!
                    """);
            openMicMusic.setCategories(List.of(liveMusicCategory, openMicCategory));
            openMicMusic.setStartingDate(LocalDateTime.of(2024,10,29,19,0));
            openMicMusic.setEndingDate(LocalDateTime.of(2024,10,30,0,0));
            openMicMusic.setVenue(monkeyHouse);
            openMicMusic.setPrice(new BigDecimal(0));
            openMicMusic.setMaxQuantity(80);

            EventEntity demolitionDerby = new EventEntity();
            demolitionDerby.setName("Demolition Derby");
            demolitionDerby.setDescription("Join us for the annual Demolition Derby at the Champlain Valley Fair on Thursday, August 29th, 2024!");
            demolitionDerby.setCategories(List.of(outdoorsCategory, demolitionDerbyCategory));
            demolitionDerby.setStartingDate(LocalDateTime.of(2025,9,29,17,30));
            demolitionDerby.setVenue(champlainExpo);
            demolitionDerby.setMaxQuantity(500);
            demolitionDerby.setPrice(BigDecimal.valueOf(15));

            eventRepository.save(openMicMusic);
            eventRepository.save(demolitionDerby);
        }
    }

    public List<EventDTO> getEvents(int page, int size, FilterDTO filterDTO, SortEnum sort) {
        return eventRepository.findWithFiltersAndSort(
                filterDTO.getCity(),
                filterDTO.getCountry(),
                filterDTO.getCategories(),
                filterDTO.getDateFrom(),
                filterDTO.getDateTo(),
                filterDTO.getPriceFrom(),
                filterDTO.getPriceTo(),
                sort.name(),
                Pageable.ofSize(size).withPage(page)
        ).map(eventEntity -> modelMapper.map(eventEntity, EventDTO.class)).toList();
    }
}
