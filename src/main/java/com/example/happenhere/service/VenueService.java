package com.example.happenhere.service;

import com.example.happenhere.dto.VenueCreationDTO;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.repository.VenueRepository;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.Optional;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public VenueService(VenueRepository venueRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    public void createVenue(VenueCreationDTO venueCreationDTO, String principal) {
        VenueEntity venueEntity = modelMapper.map(venueCreationDTO, VenueEntity.class);
        UserEntity userEntity = userRepository.findByEmail(principal);
        venueEntity.setOwner(userEntity);
        venueRepository.save(venueEntity);
    }
}
