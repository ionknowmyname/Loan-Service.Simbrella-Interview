package com.faithfulolaleru.loan_service_interview.dto;

import com.faithfulolaleru.loan_service_interview.enums.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionRequestDto {

    @NotBlank
    private String loanId;

    // @Min(10000)
    private double amount;

    @NotBlank
    private TransactionType transactionType;

    @Size(max = 500)
    private String description;
}
