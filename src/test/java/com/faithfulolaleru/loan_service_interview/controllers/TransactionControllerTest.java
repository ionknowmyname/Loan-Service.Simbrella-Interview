package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.dto.TransactionRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.TransactionResponseDto;
import com.faithfulolaleru.loan_service_interview.enums.TransactionType;
import com.faithfulolaleru.loan_service_interview.services.LoanService;
import com.faithfulolaleru.loan_service_interview.services.TransactionService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
@ExtendWith({SpringExtension.class})
// @Import(SecurityConfig.class)
@AutoConfigureMockMvc(addFilters = false)
@ContextConfiguration(classes = {TransactionController.class})
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService transactionService;

    @Test
    void createTransactionSuccess() throws Exception {

        TransactionResponseDto responseDto = getTransactionResponseDto();
        TransactionRequestDto requestDto = getTransactionRequestDto();

        when(transactionService.createTransaction(any(TransactionRequestDto.class))).thenReturn(responseDto);

        mockMvc.perform(post("/transactions/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").value(responseDto.getId().toString()))
                .andExpect(jsonPath("$.data.transactionType").value(responseDto.getTransactionType().toString()))
                .andExpect(jsonPath("$.data.loanId").value(responseDto.getLoanId().toString()));

        verify(transactionService, times(1)).createTransaction(any(TransactionRequestDto.class));
    }

    private TransactionRequestDto getTransactionRequestDto() {

        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setLoanId("fd2a7f84-e086-4263-ab9a-a92c5124a8a9");
        dto.setTransactionType(TransactionType.DISBURSEMENT);
        dto.setAmount(1000.0);
        dto.setDescription("fake description");

        return dto;
    }

    private TransactionResponseDto getTransactionResponseDto() {
        return TransactionResponseDto.builder()
                .id("fd2a7f84-e086-4263-ab9a-a92c5124a810")
                .loanId("fd2a7f84-e086-4263-ab9a-a92c5124a8a9")
                .amount(1000.0)
                .transactionType(TransactionType.DISBURSEMENT)
                .build();
    }
}