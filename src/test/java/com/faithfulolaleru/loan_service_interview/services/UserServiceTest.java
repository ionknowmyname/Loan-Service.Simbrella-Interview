package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.config.jwt.JwtService;
import com.faithfulolaleru.loan_service_interview.dto.UserCreateDto;
import com.faithfulolaleru.loan_service_interview.dto.UserResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.UserRole;
import com.faithfulolaleru.loan_service_interview.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {UserService.class, ModelMapper.class})
//@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    // @InjectMocks
    @Autowired
    private UserService userService;

//    @MockBean
    @MockitoBean
    private UserRepository userRepository;

//    @MockBean
    @MockitoBean
    private PasswordEncoder passwordEncoder;

//    @Mock
//    private UserDetails userDetails;
//
//    @Mock
//    private User user;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setup() {
//        when((User) userDetails).thenReturn(user);
//        when(user.getUserRole()).thenReturn(UserRole.ADMIN);

        // MockitoAnnotations.openMocks(this);
    }

    @Test
    void registerUserSuccess() {

        User entity = getUserEntity();
        UserCreateDto request  = getUserCreateDto();

        when(userRepository.save(any())).thenReturn(entity);

        UserResponseDto testResponse = modelMapper.map(entity, UserResponseDto.class);

        UserResponseDto serviceResponse = userService.registerUser(request);

        assertThat(serviceResponse).isNotNull();
        assertThat(serviceResponse.getId()).isEqualTo(testResponse.getId());
    }

    private User getUserEntity() {
        return User.builder()
                .id(UUID.fromString("fd2a7f84-e086-4263-ab9a-a92c5124a8a9"))
                .name("test")
                .email("test@email.com")
                .phoneNumber("081111111111")
                .userRole(UserRole.valueOf("ADMIN"))
                .password("11111111")
                .build();
    }

    private UserCreateDto getUserCreateDto() {
        UserCreateDto dto = new UserCreateDto();
        dto.setName("test");
        dto.setEmail("test@email.com");
        dto.setPhoneNumber("081111111111");
        dto.setUserRole("ADMIN");
        dto.setPassword("11111111");

        return dto;
    }
}