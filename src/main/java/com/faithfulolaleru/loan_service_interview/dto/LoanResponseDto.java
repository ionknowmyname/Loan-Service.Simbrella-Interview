package com.faithfulolaleru.loan_service_interview.dto;

import com.faithfulolaleru.loan_service_interview.enums.LoanStatus;
import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanResponseDto {

    private String id;
    private String ownerId;
    private Double amount;
    private String description;
    private Integer duration;
    private LoanType loanType;
    private LoanStatus loanStatus;
    private Double interestRate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
