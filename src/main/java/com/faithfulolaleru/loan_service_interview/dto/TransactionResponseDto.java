package com.faithfulolaleru.loan_service_interview.dto;

import com.faithfulolaleru.loan_service_interview.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponseDto {

    private String id;
    private String loanId;
    private Double amount;
    private TransactionType transactionType;
    private String description;
    private LocalDateTime createdAt;
    // private LocalDateTime updatedAt;
}
