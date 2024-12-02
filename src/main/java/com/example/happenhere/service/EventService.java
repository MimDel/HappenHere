package com.example.happenhere.service;


import com.example.happenhere.dto.common.CategoryDTO;
import com.example.happenhere.dto.common.FilterDTO;
import com.example.happenhere.dto.request.EventCreationDTO;
import com.example.happenhere.dto.response.EventDTO;
import com.example.happenhere.dto.response.MessageResponseDTO;
import com.example.happenhere.model.*;
import com.example.happenhere.repository.*;
import com.example.happenhere.utils.LogMessages;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final VenueRepository venueRepository;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;


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

        for (CategoryDTO category : eventCreationDTO.getCategories()) {
            CategoryEntity categoryEntity = categoryRepository.findByName(category.getName())
                    .orElse(modelMapper.map(category, CategoryEntity.class));
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

            // Create categories
            CategoryEntity liveMusicCategory = categoryService.saveCategory("Live Music");
            CategoryEntity openMicCategory = categoryService.saveCategory("Open Mic");
            CategoryEntity demolitionDerbyCategory = categoryService.saveCategory("Demolition Derby");
            CategoryEntity outdoorsCategory = categoryService.saveCategory("Outdoors");
            CategoryEntity comedyCategory = categoryService.saveCategory("Comedy");
            CategoryEntity techCategory = categoryService.saveCategory("Tech");
            CategoryEntity gamingCategory = categoryService.saveCategory("Gaming");

            // Create events
            EventEntity openMicMusic = createEvent(
                    "Music Open Mic",
                    """
                            Music Open Mic at The Monkey House on the last Tuesday of every month! from 7:00pm to 9:00pm hosted by Mike Paternoster. Free!
                            There will be no backline. Please bring all the instruments needed to perform.
                            Each performer gets 10ish minute sets depending on the number of performers.
                            
                            Come hang!
                            """,
                    List.of(liveMusicCategory, openMicCategory),
                    LocalDateTime.of(2024, 10, 29, 19, 0),
                    LocalDateTime.of(2024, 10, 30, 0, 0),
                    monkeyHouse,
                    BigDecimal.ZERO,
                    80
            );

            EventEntity demolitionDerby = createEvent(
                    "Demolition Derby",
                    "Join us for the annual Demolition Derby at the Champlain Valley Fair on Thursday, August 29th, 2024!",
                    List.of(outdoorsCategory, demolitionDerbyCategory),
                    LocalDateTime.of(2025, 9, 29, 17, 30),
                    LocalDateTime.of(2025, 9, 29, 21, 30),
                    champlainExpo,
                    BigDecimal.valueOf(15),
                    500
            );

            EventEntity techWorkshop = createEvent(
                    "Tech Trends 2024 Workshop",
                    """
                            Dive into the latest trends in AI, Blockchain, and Cybersecurity at our interactive workshop. 
                            Network with industry experts and learn cutting-edge technologies.
                            """,
                    List.of(techCategory),
                    LocalDateTime.of(2024, 12, 5, 10, 0),
                    LocalDateTime.of(2024, 12, 5, 16, 0),
                    champlainExpo,
                    BigDecimal.valueOf(50),
                    200
            );

            EventEntity esportsTournament = createEvent(
                    "Esports Tournament - League of Legends",
                    """
                            Join us for an epic esports showdown! Teams from across the region compete for the championship title.
                            Spectators welcome—live gameplay, commentary, and prizes!
                            """,
                    List.of(gamingCategory, techCategory),
                    LocalDateTime.of(2024, 11, 25, 13, 0),
                    LocalDateTime.of(2024, 11, 25, 22, 0),
                    champlainExpo,
                    BigDecimal.valueOf(20),
                    300
            );
            EventEntity standUpComedyNight = createEvent(
                    "Stand-Up Comedy Night",
                    "An evening of laughs with local comedians at The Monkey House! Doors open at 8 PM, show starts at 8:30 PM.",
                    List.of(comedyCategory),
                    LocalDateTime.of(2024, 11, 15, 20, 0),
                    LocalDateTime.of(2024, 11, 15, 23, 0),
                    monkeyHouse,
                    BigDecimal.valueOf(10),
                    100
            );

            // Save all events
            eventRepository.saveAll(List.of(
                    openMicMusic, demolitionDerby, standUpComedyNight, techWorkshop, esportsTournament
            ));
        }
    }

    private EventEntity createEvent(String name, String description, List<CategoryEntity> categories,
                                    LocalDateTime startingDate, LocalDateTime endingDate,
                                    VenueEntity venue, BigDecimal price, int maxNumberOfTickets) {
        EventEntity event = new EventEntity();
        event.setName(name);
        event.setDescription(description);
        event.setCategories(categories);
        event.setStartingDate(startingDate);
        event.setEndingDate(endingDate);
        event.setVenue(venue);
        event.setPrice(price);
        event.setMaxNumberOfTickets(maxNumberOfTickets);
        return event;
    }

    public List<EventDTO> getEvents(Pageable pageable, FilterDTO filterDTO, String sortBy) {
        Page<EventEntity> withFiltersAndSort = eventRepository.findWithFiltersAndSort(
                filterDTO.getCity(),
                filterDTO.getCountry(),
                filterDTO.getCategories(),
                filterDTO.getDateFrom(),
                filterDTO.getDateTo(),
                filterDTO.getPriceFrom(),
                filterDTO.getPriceTo(),
                sortBy == null ? "DATE_ASC" : sortBy,
                pageable == null ? PageRequest.of(0, 10) : pageable
        );
        return withFiltersAndSort.map(eventEntity -> modelMapper.map(eventEntity, EventDTO.class)).toList();
    }
}
