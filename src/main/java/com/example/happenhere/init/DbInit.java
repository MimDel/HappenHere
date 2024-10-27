package com.example.happenhere.init;

import com.example.happenhere.service.EventService;
import com.example.happenhere.service.UserService;
import com.example.happenhere.service.VenueService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DbInit implements CommandLineRunner {
    private final UserService userService;
    private final VenueService venueService;
    private final EventService eventService;

    @Override
    public void run(String... args) throws Exception {
        userService.seedUsers();
        venueService.seedVenues();
        eventService.seedEvents();
    }
}
