package com.faithfulolaleru.loan_service_interview.dto;

import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoanRequestDto {

    @Size(max = 100)
    private String ownerId;

    // @Min(10000)
    private double amount;

    @Size(max = 500)
    private String description;

    // @Max(value = 1000, message = "Available attendees count must not exceed 1000")
    private int duration;

    @NotBlank
    private LoanType loanType;
}
