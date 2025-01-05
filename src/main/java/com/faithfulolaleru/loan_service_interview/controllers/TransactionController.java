package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.dto.TransactionRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.TransactionResponseDto;
import com.faithfulolaleru.loan_service_interview.services.TransactionService;
import com.faithfulolaleru.loan_service_interview.utils.AppResponse;
import com.faithfulolaleru.loan_service_interview.utils.PaginationMetadata;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping(path = "transactions")
@AllArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;


    @PostMapping("/")
    public ResponseEntity<AppResponse<?>> createTransaction(@RequestBody TransactionRequestDto requestDto) {

        TransactionResponseDto response = transactionService.createTransaction(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AppResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/{loanId}")
    public AppResponse<?> getTransactionListForLoan(
            @PathVariable("loanId") String loanId,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
    ) {

        page = page == null || page == 0 ? 1 : page;
        size = size == null || size == 0 ? 100 : size;

        Page<TransactionResponseDto> response = transactionService.getTransactionListForLoan(loanId, page, size);

        PaginationMetadata pagination = new PaginationMetadata(
                response.getNumber() + 1,
                response.getSize(),
                response.getTotalPages(),
                response.getTotalElements()
        );

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                // .data(response)
                .data(Map.of(
                        "content", response.getContent(),
                        "pagination", pagination
                ))
                .build();
    }

}
