package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.JwtTestUtil;
import com.faithfulolaleru.loan_service_interview.dto.LoanRequestDto;
import com.faithfulolaleru.loan_service_interview.entity.Loan;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import com.faithfulolaleru.loan_service_interview.repository.LoanRepository;
import com.faithfulolaleru.loan_service_interview.repository.UserRepository;
import com.faithfulolaleru.loan_service_interview.services.LoanService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @SpringBootTest(properties = "spring.config.name=application-test")
@TestPropertySource(locations = "classpath:application-test.properties")
@AutoConfigureMockMvc
//@Transactional
class LoanControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    private User testUser;

    @BeforeEach
    void setUp() {
        // loanRepository.deleteAll();

        userRepository.deleteAll();  // Ensure clean state
        userRepository.flush();

        testUser = User.builder()
                // .id(UUID.randomUUID()) // Generate a UUID
                .id(null)
                .email("testUser@example.com")
                .maxLoanAmount(100000.0)
                .currentLoanAmount(0.0)
                .build();

        testUser = userRepository.saveAndFlush(testUser);
    }

    @Test
    void createLoan() throws Exception {
        LoanRequestDto requestDto = getLoanRequestDto();  // Use request DTO instead of response DTO
        UUID testUserId = testUser.getId(); // Get the UUID assigned to testUser

        mockMvc.perform(post("/loans/")
                        .header("Authorization", "Bearer " + JwtTestUtil.getValidToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.data.id").isNotEmpty()); // Expect real ID generation


        // Verify that loan was actually saved in the database
        assertEquals(1, loanRepository.count());


        // Validate that the saved loan has the same amount as the request
        Loan savedLoan = loanRepository.findAll().get(0);
        assertEquals(requestDto.getAmount(), savedLoan.getAmount());

        // Validate that the ownerId matches the test user id
        assertEquals(testUserId, savedLoan.getOwnerId());
    }

    private LoanRequestDto getLoanRequestDto() {
        return LoanRequestDto.builder()
                .loanType(LoanType.CARLOAN)
                // .interestRate(5.0)
                .amount(50000.0)
                .duration(5)
                .ownerId(testUser.getId().toString())
                // .ownerId("fd2a7f84-e086-4263-ab9a-a92c5124attt")
                // .ownerId("1")
                .description("fake description")
                .build();
    }
}