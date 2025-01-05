package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.dto.TransactionRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.TransactionResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.Loan;
import com.faithfulolaleru.loan_service_interview.entity.Transaction;
import com.faithfulolaleru.loan_service_interview.enums.TransactionType;
import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
import com.faithfulolaleru.loan_service_interview.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Slf4j
@Service
@AllArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    // private final UserService userService;
    private final LoanService loanService;
    private final TransactionRepository transactionRepository;
    private final ModelMapper modelMapper;

    @Override
    @Transactional
    public TransactionResponseDto createTransaction(TransactionRequestDto dto) {
        log.info("Making Loan Payment for --> ", dto.getLoanId());
        // User currentUser = userService.getCurrentUser();
        Transaction savedTransaction;
        try {
            Loan foundLoan = loanService.findLoanById(dto.getLoanId());

            if (dto.getTransactionType().equals(TransactionType.DISBURSEMENT)) {
                // update loan status as needed, same for repayments
                // for repayments, if loan fully paid, update status

                // you'd prally want to also make sure the 1st transaction on a loan
                // is always a disbursement
            }

            savedTransaction = transactionRepository.save(buildTransaction(dto));
        }  catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                    HttpStatus.BAD_REQUEST,
                    ErrorResponse.ERROR_TRANSACTION,
                    "Transaction Creation Failed"
            );
        }

        return modelMapper.map(savedTransaction, TransactionResponseDto.class);
    }

    @Override
    public Page<TransactionResponseDto> getTransactionListForLoan(String loanId, Integer page, Integer size) {

        List<TransactionResponseDto> allTransactionsDto;
        Page<Transaction> allTransactions = new PageImpl<>(new ArrayList<>());
        try {
            Loan foundLoan = loanService.findLoanById(loanId);

            int pageNumber = page - 1;

            allTransactions = transactionRepository.findTransactionListByLoanId(UUID.fromString(loanId),
                    PageRequest.of(pageNumber, size));


        }  catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                    HttpStatus.BAD_REQUEST,
                    ErrorResponse.ERROR_TRANSACTION,
                    "Transactions Request Failed"
            );
        }

        allTransactionsDto = allTransactions.stream()
                .map(transaction -> buildTransactionResponse(transaction))
                .collect(Collectors.toList());

        return new PageImpl<>(allTransactionsDto, allTransactions.getPageable(), allTransactions.getTotalElements());
    }

    private Transaction buildTransaction(TransactionRequestDto dto) {
        return Transaction.builder()
                .loanId(UUID.fromString(dto.getLoanId()))
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .transactionType(dto.getTransactionType())
                .createdAt(LocalDateTime.now())
                .build();
    }

    private TransactionResponseDto buildTransactionResponse(Transaction entity) {
        return TransactionResponseDto.builder()
                .id(String.valueOf(entity.getId()))
                .loanId(String.valueOf(entity.getLoanId()))
                .amount(entity.getAmount())
                .description(entity.getDescription())
                .transactionType(entity.getTransactionType())
                .createdAt(entity.getCreatedAt())
                .build();
    }

    public Transaction saveTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }
}
