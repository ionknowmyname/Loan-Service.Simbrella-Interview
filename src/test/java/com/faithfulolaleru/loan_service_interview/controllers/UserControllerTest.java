package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.config.SecurityConfig;
import com.faithfulolaleru.loan_service_interview.dto.UserCreateDto;
import com.faithfulolaleru.loan_service_interview.dto.UserResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.UserRole;
import com.faithfulolaleru.loan_service_interview.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith({SpringExtension.class})
// @Import(SecurityConfig.class) // Include security config
@AutoConfigureMockMvc(addFilters = false) // Disable filters for simplicity
@ContextConfiguration(classes = {UserController.class})
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

//    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void registerUserSuccess() throws Exception {

        // UserCreateDto requestDto = getUserCreateDto();
        UserResponseDto responseDto = getUserResponseDto();

        when(userService.registerUser(any(UserCreateDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/users/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseDto))
                )
                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.statusCode").value(HttpStatus.CREATED.value()))
//                .andExpect(jsonPath("$.httpStatus").value(HttpStatus.CREATED.name()))
                .andExpect(jsonPath("$.data.id").value(responseDto.getId().toString()));
//                .andExpect(jsonPath("$.data.name").value(responseDto.getName()))
//                .andExpect(jsonPath("$.data.email").value(responseDto.getEmail()));

        verify(userService, times(1)).registerUser(any(UserCreateDto.class));
    }

    private UserResponseDto getUserResponseDto() {
        return UserResponseDto.builder()
                .id("fd2a7f84-e086-4263-ab9a-a92c5124a8a9")
                .name("test")
                .email("test@email.com")
                .phoneNumber("081111111111")
                .userRole("ADMIN")
                .build();
    }


}