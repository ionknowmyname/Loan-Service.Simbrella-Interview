package com.faithfulolaleru.loan_service_interview.repository;


import com.faithfulolaleru.loan_service_interview.entity.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {

    // Optional<Loan> findByEmail(String email);

    @Query(value = "SELECT t.* FROM Transactions t WHERE t.loan_id = :loanId",
            nativeQuery = true)
    Page<Transaction> findTransactionListByLoanId(UUID loanId, Pageable pageable);


}
