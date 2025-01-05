package com.faithfulolaleru.loan_service_interview.entity;

import com.faithfulolaleru.loan_service_interview.enums.LoanStatus;
import com.faithfulolaleru.loan_service_interview.enums.LoanType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity(name = "loans")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "owner_id")
    private UUID ownerId;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "duration")
    private Integer duration;  // in months

    @Column(name = "interest_rate", columnDefinition = "DOUBLE PRECISION DEFAULT 5.0") //
    private Double interestRate; //  = 5.0;  // in %

    @Column(name = "loan_type")
    @Enumerated(EnumType.STRING)
    private LoanType loanType;

    @Column(name = "loan_status")
    @Enumerated(EnumType.STRING)
    private LoanStatus loanStatus;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
