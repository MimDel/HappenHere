package com.example.happenhere.service;

import com.example.happenhere.dto.request.VenueCreationDTO;
import com.example.happenhere.dto.response.VenueDTO;
import com.example.happenhere.model.AddressEntity;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.model.VenueEntity;
import com.example.happenhere.repository.AddressRepository;
import com.example.happenhere.repository.EventRepository;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.repository.VenueRepository;
import com.example.happenhere.utils.LogMessages;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class VenueService {
    private final VenueRepository venueRepository;
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EventService eventService;
    private final EventRepository eventRepository;

    @Transactional
    public void createVenue(VenueCreationDTO venueCreationDTO, String principalEmail) {
        VenueEntity venueEntity = modelMapper.map(venueCreationDTO, VenueEntity.class);
        Optional<UserEntity> userEntity = userRepository.findByEmail(principalEmail);
        if (userEntity.isEmpty()) {
            log.error(LogMessages.USER_EMAIL_NOT_FOUND, principalEmail);
            return;

        }


        AddressEntity addressEntity = venueEntity.getAddress();
        addressEntity.setVenue(venueEntity);
        addressRepository.save(addressEntity);

        venueEntity.setOwner(userEntity.get());
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

    @Transactional
    public void seedVenues() {
        if (venueRepository.count() == 0) {
            String userEmail  = "user@gmail.com";
            Optional<UserEntity> user = userRepository.findByEmail(userEmail);
            if (user.isEmpty()) {
                log.error(LogMessages.USER_EMAIL_NOT_FOUND, userEmail);
                return;
            }

            AddressEntity monkeyHouseAdress = new AddressEntity();
            monkeyHouseAdress.setTown("Winooski");
            monkeyHouseAdress.setAddressLine1("30 Main stCafe");
            monkeyHouseAdress.setCountry("USA");
            monkeyHouseAdress.setPostcode("05404");

            AddressEntity champlainExpoAddress = new AddressEntity();
            champlainExpoAddress.setTown("Essex Junction");
            champlainExpoAddress.setAddressLine1("105 Pearl st");
            champlainExpoAddress.setCountry("USA");
            champlainExpoAddress.setPostcode("05451");

            VenueEntity monkeyHouse = new VenueEntity();
            monkeyHouse.setAddress(monkeyHouseAdress);
            monkeyHouse.setOwner(user.get());
            monkeyHouse.setName("Monkey House");
            monkeyHouse.setDescription("Born of the high concentration of amazing musicians in the area, The Monkey House has since built the Burlington music scene into a given tour stop for many up-and-coming bands both local and international. Through work with folks like Waking Windows, Angioplasty Media, MSR Presents, Tick Tick, Anthill Collective, & Green Mountain Cabaret, The Monkey has filled the calendar with bands, DJs, comedians and live multimedia almost every night of the week. ");
            monkeyHouse.setPhoneNumber("8888888888");
            monkeyHouse.setType("Bar");

            VenueEntity champlainExpo = new VenueEntity();
            champlainExpo.setAddress(champlainExpoAddress);
            champlainExpo.setType("Exposition");
            champlainExpo.setName("Champlain Valley Exposition");
            champlainExpo.setPhoneNumber("9999999999");
            champlainExpo.setDescription("At the Champlain Valley Exposition, we pride ourselves on flexibility. If you imagine it, we can make it happen.");
            champlainExpo.setOwner(user.get());

            champlainExpo.setOwner(user.get());
            monkeyHouse.setOwner(user.get());

            addressRepository.save(champlainExpoAddress);
            addressRepository.save(monkeyHouseAdress);

            venueRepository.save(champlainExpo);
            venueRepository.save(monkeyHouse);
        }
    }
}
