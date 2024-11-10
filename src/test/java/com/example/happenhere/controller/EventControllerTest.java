package com.example.happenhere.controller;

import com.example.happenhere.LocalDateTimeAdapter;
import com.example.happenhere.dto.common.AddressDTO;
import com.example.happenhere.dto.common.CategoryDTO;
import com.example.happenhere.dto.request.EventCreationDTO;
import com.example.happenhere.dto.request.VenueCreationDTO;
import com.example.happenhere.dto.response.LoginDTO;
import com.example.happenhere.init.DbInit;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.repository.EventRepository;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.repository.VenueRepository;
import com.example.happenhere.service.UserService;
import com.example.happenhere.service.VenueService;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EventControllerTest {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    VenueRepository venueRepository;

    @Autowired
    VenueService venueService;

    @Autowired
    UserService userService;

    @Autowired
    MockMvc mockMvc;

    @MockBean
    DbInit dbInit;

    String bearerToken;

    Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .create();

    Long venueId;

    @Test
    void createEvent() throws Exception {
        CategoryDTO liveMusicCategory = new CategoryDTO("Live Music");
        CategoryDTO openMicCategory = new CategoryDTO("Open Mic");
        Set<CategoryDTO> categories = new HashSet<>();
        categories.add(liveMusicCategory);
        categories.add(openMicCategory);
        EventCreationDTO eventCreationDTO = new EventCreationDTO(
                "Music Open Mic",
                """
                    Music Open Mic at The Monkey House on the last Tuesday of every month! from 7:00pm to 9:00pm hosted by Mike Paternoster. Free!
                    There will be no backline. Please bring all the instruments needed to perform.
                    Each performer gets 10ish minute sets depending on the number of performers.
                    
                    Come hang!
                    """,
                new BigDecimal(0),
                80,
                venueId,
                LocalDateTime.of(2025,10,29,19,0),
                LocalDateTime.of(2025,10,30,0,0),
                categories
        );

        mockMvc.perform(post("/event/create")
                        .header("Authorization",bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(eventCreationDTO)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(eventRepository.count(), 1);
    }

    @BeforeEach
    void setUp() throws Exception {
        userService.seedUsers();
        venueService.seedVenues();

        VenueEntity venue = venueRepository.findByPhoneNumber("8888888888").get();
        venueId = venue.getId();

        LoginDTO loginDTO = new LoginDTO("user@gmail.com", "password");

        var result = mockMvc.perform(post("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(loginDTO))).andReturn();
        String json = result.getResponse().getContentAsString();
        bearerToken = "Bearer " + gson.fromJson(json, Map.class).get("token");

    }

    @AfterEach
    @Transactional
    void tearDown() {
        userRepository.deleteAll();
        eventRepository.deleteAll();
        venueRepository.deleteAll();
    }
}
