package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.dto.LoanRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.LoanResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.Loan;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.LoanStatus;
import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import com.faithfulolaleru.loan_service_interview.enums.UserRole;
import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
import com.faithfulolaleru.loan_service_interview.repository.LoanRepository;
import com.faithfulolaleru.loan_service_interview.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {LoanServiceImpl.class, ModelMapper.class})
class LoanServiceImplTest {

    @Autowired
    private LoanService loanService;

//    @MockBean
    @MockitoBean
    private UserService userService;

//    @MockBean
    @MockitoBean
    private LoanRepository loanRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setup() {

    }


    @Test
    void createLoanSuccess() {
        LoanRequestDto requestDto = getLoanRequestDto();
        Loan mockLoan = getLoanEntity();
        LoanResponseDto responseDto = getLoanResponseDto();
        User mockUser = getUserEntity();

        // Mocking userService to return currentUser
        when(userService.getCurrentUser()).thenReturn(mockUser);
        when(userService.saveUser(any(User.class))).thenReturn(mockUser);
        when(loanRepository.save(any(Loan.class))).thenReturn(mockLoan);

        // Act
        LoanResponseDto result = loanService.createLoan(requestDto);

        // Assert
        assertNotNull(result);
        // assertEquals(responseDto.getId(), result.getId());
        // assertEquals(responseDto.getOwnerId(), result.getOwnerId());

        verify(userService, times(1)).getCurrentUser();
        verify(userService, times(1)).saveUser(any(User.class));
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void createLoanFailure_MaxLoanExceeded() {
        // Arrange
        LoanRequestDto requestDto = getLoanRequestDto();
        User mockUser = getUserEntity();
        mockUser.setCurrentLoanAmount(mockUser.getMaxLoanAmount()); // Simulate max loan exceeded

        when(userService.getCurrentUser()).thenReturn(mockUser);

        // Act & Assert
        GeneralException exception = assertThrows(GeneralException.class, () -> {
            loanService.createLoan(requestDto);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorResponse.ERROR_LOAN, exception.getError());
        assertEquals("Loan Creation Failed", exception.getMessage());

        verify(userService, times(1)).getCurrentUser();
        verify(loanRepository, never()).save(any(Loan.class)); // Ensure repository is not called
    }

    @Test
    void updateLoanStatus() {
    }

    @Test
    void findLoanById() {
    }

    private User getUserEntity() {
        return User.builder()
                .id(UUID.fromString("fd2a7f84-e086-4263-ab9a-a92c5124a8a9"))
                .name("test")
                .email("test@email.com")
                .phoneNumber("081111111111")
                .userRole(UserRole.valueOf("ADMIN"))
                .password("11111111")
                .maxLoanAmount(100000.0)
                .currentLoanAmount(30000.0)
                .build();
    }

    private Loan getLoanEntity() {
        return Loan.builder()
                .id(UUID.fromString("fd2a7f84-e086-4263-ab9a-a92c5124a8a9"))
                .loanStatus(LoanStatus.PENDING)
                .loanType(LoanType.CARLOAN)
                .interestRate(5.0)
                .description("fake description")
                .amount(1000.0)
                .build();
    }

    private LoanRequestDto getLoanRequestDto() {

        LoanRequestDto dto = new LoanRequestDto();
        dto.setOwnerId("fd2a7f84-e086-4263-ab9a-a92c5124attt");
        dto.setAmount(50000.0);
        dto.setDescription("fake description");
        dto.setDuration(5);
        dto.setLoanType(LoanType.CARLOAN);

        return dto;
    }

    private LoanResponseDto getLoanResponseDto() {
        return LoanResponseDto.builder()
                .id("fd2a7f84-e086-4263-ab9a-a92c5124a8a9")
                .loanStatus(LoanStatus.PENDING)
                .loanType(LoanType.CARLOAN)
                .interestRate(5.0)
                .amount(50000.0)
                .duration(5)
                .ownerId("fd2a7f84-e086-4263-ab9a-a92c5124attt")
                .description("fake description")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}