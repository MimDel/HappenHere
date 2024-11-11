package com.example.happenhere.controller;

import com.example.happenhere.dto.common.AddressDTO;
import com.example.happenhere.dto.request.VenueCreationDTO;
import com.example.happenhere.dto.response.LoginDTO;
import com.example.happenhere.init.DbInit;
import com.example.happenhere.model.AddressEntity;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.repository.AddressRepository;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.repository.VenueRepository;
import com.example.happenhere.service.UserService;
import com.example.happenhere.service.VenueService;
import com.google.gson.Gson;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class VenueControllerTest {
    @Autowired
    UserRepository userRepository;
    @Autowired
    VenueRepository venueRepository;
    @Autowired
    AddressRepository addressRepository;
    @Autowired
    UserService userService;

    @Autowired
    MockMvc mockMvc;
    @MockBean
    DbInit dbInit;

    Gson gson = new Gson();
    String bearerToken;
    @Autowired
    private VenueService venueService;

    @Test
    void createSuccess() throws Exception {
        AddressDTO addressDTO = new AddressDTO(
                "30 Main stCafe",
                null,
                "Winooski",
                "USA"
                , "05404"
        );
        VenueCreationDTO venueCreationDTO = new VenueCreationDTO(
                "monkey house",
                "monkeyhouse@gmail.com",
                addressDTO,
                "8888888888",
                "Born of the high concentration of amazing musicians in the area, The Monkey House has since built the Burlington music scene into a given tour stop for many up-and-coming bands both local and international. Through work with folks like Waking Windows, Angioplasty Media, MSR Presents, Tick Tick, Anthill Collective, & Green Mountain Cabaret, The Monkey has filled the calendar with bands, DJs, comedians and live multimedia almost every night of the week.",
                "bar"
        );

        mockMvc.perform(post("/venue/create")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(venueCreationDTO)))
                .andExpect(status().isCreated());
        Assertions.assertEquals(venueRepository.count(), 1);
        Optional<VenueEntity> venue = venueRepository.findByPhoneNumber("8888888888");
        if(venue.isEmpty()) {
            Assertions.fail("Venue not found");
        }
        Assertions.assertEquals(venue.get().getName(), venueCreationDTO.getName());
        Assertions.assertEquals(venue.get().getPhoneNumber(), venueCreationDTO.getPhoneNumber());
    }

    @Test
    void createFail() throws Exception{
        AddressDTO addressDTO = new AddressDTO(
                "30 Main stCafe",
                null,
                "Winooski",
                "USA"
                , "05404"
        );
        VenueCreationDTO venueCreationDTO = new VenueCreationDTO(
                "monkey house",
                "monkeyhouse",
                addressDTO,
                "8888888888",
                "Born of the high concentration of amazing musicians in the area, The Monkey House has since built the Burlington music scene into a given tour stop for many up-and-coming bands both local and international. Through work with folks like Waking Windows, Angioplasty Media, MSR Presents, Tick Tick, Anthill Collective, & Green Mountain Cabaret, The Monkey has filled the calendar with bands, DJs, comedians and live multimedia almost every night of the week.",
                "bar"
        );

        mockMvc.perform(post("/venue/create")
                .header("Authorization", bearerToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(gson.toJson(venueCreationDTO)))
                .andExpect(status().isBadRequest());

        addressDTO.setAddressLine1(null);
        venueCreationDTO.setEmail("monkeyhouse@gmail.com");
        mockMvc.perform(post("/venue/create")
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(venueCreationDTO)))
                .andExpect(status().isBadRequest());

        addressDTO = new AddressDTO(
                "30 Main stCafe",
                null,
                "Winooski",
                "USA"
                , "05404"
        );
        venueCreationDTO = new VenueCreationDTO(
                "monkey house",
                "monkeyhouse@gmail.com",
                addressDTO,
                "8888888888",
                "Born of the high concentration of amazing musicians in the area, The Monkey House has since built the Burlington music scene into a given tour stop for many up-and-coming bands both local and international. Through work with folks like Waking Windows, Angioplasty Media, MSR Presents, Tick Tick, Anthill Collective, & Green Mountain Cabaret, The Monkey has filled the calendar with bands, DJs, comedians and live multimedia almost every night of the week.",
                "bar"
        );
        mockMvc.perform(post("/venue/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(gson.toJson(venueCreationDTO)))
                .andExpect(status().isForbidden());

        Assertions.assertEquals(venueRepository.count(), 0);
    }

    @Test
    void getVenueSuccess() throws Exception{
        venueService.seedVenues();
        Optional<VenueEntity> venueEntity = venueRepository.findByPhoneNumber("8888888888");
        Long venueId = venueEntity.get().getId();

        mockMvc.perform(get("/venue/" + venueId)
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Monkey House"))
                .andExpect(jsonPath("$.phoneNumber").value("8888888888"))
                .andExpect(jsonPath("$.address.town").value("Winooski"));
    }

    @Test
    void getVenueFail() throws Exception{
        mockMvc.perform(get("/venue/" + 1)
                        .header("Authorization", bearerToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteVenueSuccess() throws Exception{
        venueService.seedVenues();
        Optional<VenueEntity> venueEntity = venueRepository.findByPhoneNumber("8888888888");
        Long venueId = venueEntity.get().getId();

        mockMvc.perform(delete("/venue/" + venueId)
                .header("Authorization", bearerToken))
                .andExpect(status().isOk());
    }

    //todo 403
    @Test
    void deleteVenueFail() throws Exception{
        mockMvc.perform(delete("/venue/" + 1)
                        .header("Authorization", bearerToken))
                .andExpect(status().isNotFound());
    }

    @BeforeEach
    void setUp() throws Exception {
        userService.seedUsers();

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
        venueRepository.deleteAll();
        addressRepository.deleteAll();
    }

}
