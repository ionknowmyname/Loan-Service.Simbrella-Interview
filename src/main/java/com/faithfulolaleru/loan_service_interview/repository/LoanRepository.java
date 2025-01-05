package com.faithfulolaleru.loan_service_interview.repository;


import com.faithfulolaleru.loan_service_interview.entity.Loan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface LoanRepository extends JpaRepository<Loan, UUID> {

    // Optional<Loan> findByEmail(String email);

    @Query(value = "SELECT l.* FROM Loans l WHERE l.owner_id = :ownerId",
            nativeQuery = true)
    Page<Loan> findLoanListByOwnerId(UUID ownerId, Pageable pageable);

    @Query(value = "SELECT l.* FROM Loans l WHERE l.owner_id = :ownerId AND (l.created_at > :startDate) " +
            "AND (l.created_at <= :endDate) order by l.created_at desc", nativeQuery = true) //  order by e.createdAt desc
    Page<Loan> findLoanListByOwnerIdAndDateRange(UUID ownerId, @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate, Pageable pageable);

    @Query(value = "SELECT l.* FROM Loans l WHERE l.owner_id = :ownerId" +
            "AND l.created_at BETWEEN :start AND :end " +
            "AND (l.loan_type LIKE %:queryParameter% or l.loan_status = :queryParameter)", nativeQuery = true)
    Page<Loan> findLoanListByOwnerIdDateRangeAndQueryParameter(UUID ownerId, @Param("queryParameter") String queryParameter,
                                    @Param("startDate") LocalDate startDate,
                                    @Param("endDate") LocalDate endDate,
                                    Pageable pageable);

    @Query(value = "SELECT l.* FROM Loans l WHERE l.owner_id = :ownerId" +
            "AND (l.loan_type LIKE %:queryParameter% or l.loan_status = :queryParameter)", nativeQuery = true)
    Page<Loan> findLoanListByOwnerIdAndQueryParameter(UUID ownerId, @Param("queryParameter") String queryParameter,
                                                               Pageable pageable);

    // List<Loan> findByOwnerId(UUID ownerId);

}
