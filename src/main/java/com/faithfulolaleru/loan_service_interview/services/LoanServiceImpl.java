package com.faithfulolaleru.loan_service_interview.services;


import com.faithfulolaleru.loan_service_interview.dto.LoanRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.LoanResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.Loan;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.LoanStatus;
import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
import com.faithfulolaleru.loan_service_interview.repository.LoanRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
 @AllArgsConstructor
public class LoanServiceImpl implements LoanService {

    private final LoanRepository loanRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public LoanResponseDto createLoan(LoanRequestDto dto) {

        User currentUser = userService.getCurrentUser();
        // User foundUser = userService.findUserById(dto.getOwnerId());
        Loan savedLoan;
        try {
            // make sure user can get loan
            Double userLoanMax = currentUser.getMaxLoanAmount();
            Double userLoanCurrent = currentUser.getCurrentLoanAmount();

            if (userLoanCurrent + dto.getAmount() > userLoanMax) {
                throw new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_LOAN,
                        "Loan Max Exceeded"
                );
            }

            currentUser.setCurrentLoanAmount(userLoanCurrent + dto.getAmount());
            userService.saveUser(currentUser);

            // savedLoan.setOwnerId(foundUser.getId());
            dto.setOwnerId(String.valueOf(currentUser.getId()));
            savedLoan = loanRepository.save(buildLoan(dto));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                    HttpStatus.BAD_REQUEST,
                    ErrorResponse.ERROR_LOAN,
                    "Loan Creation Failed"
            );
        }

        return modelMapper.map(savedLoan, LoanResponseDto.class);
    }

    @Override
    public Page<LoanResponseDto> getLoanListPerUser(String queryParameter, LocalDate startDate,
                                                    LocalDate endDate, Integer page, Integer size) {

        // query parameter can be loanType or loanStatus
        //  interestRate, duration & amount can be another query parameter
        User currentUser = userService.getCurrentUser();

        int pageNumber = page - 1;
        List<LoanResponseDto> allLoansDto;
        Page<Loan> allLoans; //  = new PageImpl<>(new ArrayList<>());

        // Determine the scenario
        Scenario scenario = determineScenario(queryParameter, startDate, endDate);

        switch (scenario) {
            case NO_FILTERS:
                allLoans = loanRepository.findLoanListByOwnerId(
                        currentUser.getId(),
                        PageRequest.of(pageNumber, size)
                );
                break;

            case DATE_RANGE_ONLY:
                allLoans = loanRepository.findLoanListByOwnerIdAndDateRange(
                        currentUser.getId(),
                        startDate,
                        endDate,
                        PageRequest.of(pageNumber, size)
                );
                break;

            case DATE_RANGE_AND_QUERY:
                allLoans = loanRepository.findLoanListByOwnerIdDateRangeAndQueryParameter(
                        currentUser.getId(),
                        queryParameter,
                        startDate,
                        endDate,
                        PageRequest.of(pageNumber, size)
                );
                break;

            case QUERY_ONLY:
                allLoans = loanRepository.findLoanListByOwnerIdAndQueryParameter(
                        currentUser.getId(),
                        queryParameter,
                        PageRequest.of(pageNumber, size)
                );
                break;

            default:
                allLoans = new PageImpl<>(new ArrayList<>());
        }

        allLoansDto = allLoans.stream()
                .map(loan -> buildLoanResponse(true, loan))
                .collect(Collectors.toList());

        return new PageImpl<>(allLoansDto, allLoans.getPageable(), allLoans.getTotalElements());
    }

    @Override
    public LoanResponseDto updateLoanStatus(String loanId, String newStatus) {
        log.info("Updating Loan Status for --> ", loanId);
        Loan foundLoan = findLoanById(loanId);

        foundLoan.setLoanStatus(LoanStatus.valueOf(newStatus));
        foundLoan.setUpdatedAt(LocalDateTime.now());

        Loan updatedLoan = saveLoan(foundLoan);

        return buildLoanResponse(true, updatedLoan);
    }



    @Override
    public Loan findLoanById(String loanId) {
        return loanRepository.findById(UUID.fromString(loanId))
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.NOT_FOUND,
                        ErrorResponse.ERROR_LOAN,
                        "Loan not found"
                ));

    }

    private Double calculateInterestRate(Double amount, Integer duration) {
        return (amount * 0.05 * duration) / 100;
    }

    private Loan buildLoan(LoanRequestDto dto) {
        return Loan.builder()
                .ownerId(UUID.fromString(dto.getOwnerId()))
                .amount(dto.getAmount())
                .description(dto.getDescription())
                .duration(dto.getDuration())
                .interestRate(calculateInterestRate(dto.getAmount(), dto.getDuration()))
                .loanType(dto.getLoanType())
                .loanStatus(LoanStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }

    private LoanResponseDto buildLoanResponse(boolean allFields, Loan entity) {
        if (allFields) {
            return LoanResponseDto.builder()
                    .id(String.valueOf(entity.getId()))
                    .ownerId(String.valueOf(entity.getOwnerId()))
                    .amount(entity.getAmount())
                    .description(entity.getDescription())
                    .duration(entity.getDuration())
                    .interestRate(entity.getInterestRate())
                    .loanType(entity.getLoanType())
                    .loanStatus(entity.getLoanStatus())
                    .createdAt(entity.getCreatedAt())
                    .updatedAt(entity.getUpdatedAt())
                    .build();
        } else  {
            return LoanResponseDto.builder()
                    .id(String.valueOf(entity.getId()))
                    .ownerId(String.valueOf(entity.getOwnerId()))
                    .amount(entity.getAmount())
                    // .description(entity.getDescription())
                    .duration(entity.getDuration())
                    // .interestRate(entity.getInterestRate())
                    .loanType(entity.getLoanType())
                    .loanStatus(entity.getLoanStatus())
                    // .createdAt(entity.getCreatedAt())
                    // .updatedAt(entity.getUpdatedAt())
                    .build();
        }

    }

    public Loan saveLoan(Loan loan) {
        return loanRepository.save(loan);
    }

    // Enum for scenarios
    private enum Scenario {
        NO_FILTERS,
        DATE_RANGE_ONLY,
        DATE_RANGE_AND_QUERY,
        QUERY_ONLY
    }

    // Helper method to determine scenario
    private Scenario determineScenario(String queryParameter, LocalDate startDate, LocalDate endDate) {
        if (startDate == null && endDate == null && queryParameter == null) {
            return Scenario.NO_FILTERS;
        } else if (startDate != null && endDate != null && queryParameter == null) {
            return Scenario.DATE_RANGE_ONLY;
        } else if (startDate != null && endDate != null && queryParameter != null) {
            return Scenario.DATE_RANGE_AND_QUERY;
        } else if (queryParameter != null) {
            return Scenario.QUERY_ONLY;
        } else {
            return null; // Fallback case
        }
    }

}
