package com.example.happenhere.service;

import com.example.happenhere.dto.RegistrationDTO;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.repository.UserRepository;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final  PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<RegistrationDTO> registerUser(RegistrationDTO registrationDTO) {
        if(userRepository.existsByEmail(registrationDTO.email())) {
            return Optional.empty();
        }

        UserEntity userEntity = modelMapper.map(registrationDTO, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registrationDTO.password()));

        userRepository.save(userEntity);
        return Optional.of(registrationDTO);
    }
}
