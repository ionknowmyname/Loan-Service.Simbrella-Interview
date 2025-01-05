package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.dto.TransactionRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.TransactionResponseDto;
import org.springframework.data.domain.Page;

public interface TransactionService {

    TransactionResponseDto createTransaction(TransactionRequestDto dto);

    Page<TransactionResponseDto> getTransactionListForLoan(String loanId, Integer page, Integer size);
}
