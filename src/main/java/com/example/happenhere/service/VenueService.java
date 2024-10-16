package com.example.happenhere.service;

import com.example.happenhere.dto.VenueCreationDTO;
import com.example.happenhere.dto.VenueDTO;
import com.example.happenhere.model.AddressEntity;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.repository.AddressRepository;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.repository.VenueRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class VenueService {
    private final VenueRepository venueRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public VenueService(VenueRepository venueRepository, UserRepository userRepository,
                        ModelMapper modelMapper, AddressRepository addressRepository) {
        this.venueRepository = venueRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
    }

    @Transactional
    public void createVenue(VenueCreationDTO venueCreationDTO, String principal) {
        VenueEntity venueEntity = modelMapper.map(venueCreationDTO, VenueEntity.class);
        UserEntity userEntity = userRepository.findByEmail(principal);

        AddressEntity addressEntity = venueEntity.getAddress();
        addressEntity.setVenue(venueEntity);
        addressRepository.save(addressEntity);

        venueEntity.setOwner(userEntity);
        venueEntity.setAddress(addressEntity);

        venueRepository.save(venueEntity);

    }


    public boolean delete(long id) {
        if(venueRepository.existsById(id)) {
            venueRepository.deleteById(id);
            return true;
        }
        return false;
    }


    public Optional<VenueDTO> getVenue(long id) {
        Optional<VenueEntity> venueEntity = venueRepository.findById(id);
        if(venueEntity.isEmpty()){
            return Optional.empty();
        }
        VenueDTO venueDTO = modelMapper.map(venueEntity.get(), VenueDTO.class);
        return Optional.of(venueDTO);
    }
}
