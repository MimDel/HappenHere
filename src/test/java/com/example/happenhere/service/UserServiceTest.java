package com.example.happenhere.service;
import com.example.happenhere.dto.request.RegistrationDTO;
import com.example.happenhere.model.UserEntity;
import com.example.happenhere.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;
    @Mock
    UserRepository userRepository;
    @Mock
    ModelMapper modelMapper;
    @Mock
    PasswordEncoder passwordEncoder;
    UserEntity userEntity;
    RegistrationDTO registrationDTO;

    @Test
    public void testRegisterUser() {
        Optional<RegistrationDTO> result = userService.registerUser(registrationDTO);
        Assertions.assertEquals(registrationDTO, result.get());
        Assertions.assertEquals(userEntity.getEmail(), result.get().getEmail());
        Assertions.assertEquals(userEntity.getFirstName(), result.get().getFirstName());
        Assertions.assertEquals(userEntity.getLastName(), result.get().getLastName());
        Assertions.assertEquals(userEntity.getPassword(), "encodedPassword");
    }


    @BeforeEach
    void setUp() {
        userEntity = new UserEntity();
        userEntity.setEmail("user@gmail.com");
        userEntity.setPassword("password");
        userEntity.setId(1L);
        userEntity.setFirstName("user");
        userEntity.setLastName("userov");

        registrationDTO = new RegistrationDTO("user@gmail.com", "password", "user", "userov");

        given(userRepository.save(userEntity)).willReturn(userEntity);
        given(modelMapper.map(registrationDTO, UserEntity.class)).willReturn(userEntity);
        given(passwordEncoder.encode(registrationDTO.getPassword())).willReturn("encodedPassword");
    }

}
