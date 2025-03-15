package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.dto.LoanRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.LoanResponseDto;
import com.faithfulolaleru.loan_service_interview.enums.LoanStatus;
import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import com.faithfulolaleru.loan_service_interview.services.LoanService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoanController.class)
@ExtendWith({SpringExtension.class})
// @Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {LoanController.class})
class LoanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LoanService loanService;


    @Test
    void createLoanSuccess() throws Exception {

        LoanResponseDto responseDto = getLoanResponseDto();

        when(loanService.createLoan(any(LoanRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/loans/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(responseDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(responseDto.getId().toString()));

        verify(loanService, times(1)).createLoan(any(LoanRequestDto.class));
    }

    @Test
    void updateLoanStatusSuccess() {
    }

    private LoanResponseDto getLoanResponseDto() {
        return LoanResponseDto.builder()
                .id("fd2a7f84-e086-4263-ab9a-a92c5124a8a9")
                .loanStatus(LoanStatus.PENDING)
                .loanType(LoanType.CARLOAN)
                .interestRate(5.0)
                .amount(100000.0)
                .duration(5)
                .ownerId("fd2a7f84-e086-4263-ab9a-a92c5124attt")
                .description("fake description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}