package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.dto.LoanRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.LoanResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.Loan;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface LoanService {

    LoanResponseDto createLoan(LoanRequestDto dto);

    Page<LoanResponseDto> getLoanListPerUser(String queryParameter, LocalDate startDate,
                                             LocalDate endDate, Integer page, Integer size);

    LoanResponseDto updateLoanStatus(String loanId, String newStatus);

    Loan findLoanById(String loanId);
}
