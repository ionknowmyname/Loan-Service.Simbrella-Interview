package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.dto.TransactionRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.TransactionResponseDto;
import com.faithfulolaleru.loan_service_interview.dto.UserCreateDto;
import com.faithfulolaleru.loan_service_interview.dto.UserResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.Loan;
import com.faithfulolaleru.loan_service_interview.entity.Transaction;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.LoanStatus;
import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import com.faithfulolaleru.loan_service_interview.enums.TransactionType;
import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
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

import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
@ContextConfiguration(classes = {TransactionServiceImpl.class, ModelMapper.class})
class TransactionServiceImplTest {

    @Autowired
    private TransactionService transactionService;

//    @MockBean
    @MockitoBean
    private LoanService loanService;

//    @MockBean
    @MockitoBean
    private TransactionRepository transactionRepository;

    private ModelMapper modelMapper = new ModelMapper();

    @BeforeEach
    void setup() {

    }

    @Test
    void createTransactionSuccess() {

        // Arrange
        TransactionRequestDto requestDto = getTransactionRequestDto();
        Loan mockLoan = getLoanEntity();
        Transaction mockTransaction = getTransactionEntity();
        TransactionResponseDto responseDto = getTransactionResponseDto();

        when(loanService.findLoanById(requestDto.getLoanId())).thenReturn(mockLoan);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(mockTransaction);

        TransactionResponseDto responseDto2 = modelMapper.map(mockTransaction, TransactionResponseDto.class);

        TransactionResponseDto result = transactionService.createTransaction(requestDto);

        assertNotNull(result);
        assertEquals(responseDto2.getId(), result.getId());

        verify(loanService, times(1)).findLoanById(requestDto.getLoanId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void createTransactionFailure() {

        TransactionRequestDto requestDto = getTransactionRequestDto();

        when(loanService.findLoanById(requestDto.getLoanId())).thenThrow(new GeneralException(
                HttpStatus.NOT_FOUND,
                ErrorResponse.ERROR_LOAN,
                "Loan not found"
        ));

        GeneralException exception = assertThrows(GeneralException.class, () ->
                transactionService.createTransaction(requestDto)
        );

        assertNotNull(exception);
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
        assertEquals(ErrorResponse.ERROR_TRANSACTION, exception.getError());
        assertEquals("Transaction Creation Failed", exception.getMessage());

        verify(loanService, times(1)).findLoanById(requestDto.getLoanId());
        verify(transactionRepository, never()).save(any(Transaction.class));
    }

    private TransactionRequestDto getTransactionRequestDto() {

        TransactionRequestDto dto = new TransactionRequestDto();
        dto.setLoanId("fd2a7f84-e086-4263-ab9a-a92c5124a8a9");
        dto.setTransactionType(TransactionType.DISBURSEMENT);
        dto.setAmount(1000.0);
        dto.setDescription("fake description");

        return dto;
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

    private Transaction getTransactionEntity() {
        return Transaction.builder()
                .id(UUID.fromString("fd2a7f84-e086-4263-ab9a-a92c5124a810"))
                .loanId(UUID.fromString("fd2a7f84-e086-4263-ab9a-a92c5124a8a9"))
                .amount(1000.0)
                .transactionType(TransactionType.DISBURSEMENT)
                .build();
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