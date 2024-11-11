package com.example.happenhere.service;

import com.example.happenhere.dto.request.RegistrationDTO;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.repository.UserRepository;
import com.example.happenhere.utils.LogMessages;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final  PasswordEncoder passwordEncoder;

    public Optional<RegistrationDTO> registerUser(RegistrationDTO registrationDTO) {
        if(userRepository.existsByEmail(registrationDTO.getEmail())) {
            return Optional.empty();
        }

        UserEntity userEntity = modelMapper.map(registrationDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.getPassword()));

        userRepository.save(userEntity);
        return Optional.of(registrationDTO);
    }

    public void seedUsers() {
        if (userRepository.count() == 0) {
            log.info(LogMessages.DB_INITIALIZING, "users");
            UserEntity userEntity = new UserEntity();
            userEntity.setFirstName("User");
            userEntity.setLastName("Userov");
            userEntity.setEmail("user@gmail.com");
            userEntity.setPassword(passwordEncoder.encode("password"));

            UserEntity secondUserEntity = new UserEntity();
            secondUserEntity.setFirstName("Ivan");
            secondUserEntity.setLastName("Ivanov");
            secondUserEntity.setEmail("ivan@gmail.com");
            secondUserEntity.setPassword(passwordEncoder.encode("ivanspassword"));

            userRepository.save(userEntity);
            userRepository.save(secondUserEntity);
        }
    }
}
