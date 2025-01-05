package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.dto.LoanRequestDto;
import com.faithfulolaleru.loan_service_interview.dto.LoanResponseDto;
import com.faithfulolaleru.loan_service_interview.services.LoanService;
import com.faithfulolaleru.loan_service_interview.utils.AppResponse;
import com.faithfulolaleru.loan_service_interview.utils.PaginationMetadata;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping(path = "loans")
// @AllArgsConstructor
public class LoanController {

    private final LoanService loanService;

    private final Bucket bucket;

    public LoanController(LoanService loanService) {
        this.loanService = loanService;
        Bandwidth limit = Bandwidth.classic(1, Refill.greedy(1, Duration.ofSeconds(60)));
        this.bucket = Bucket.builder()
                .addLimit(limit)
                .build();
    }

    @PostMapping("/")
    public ResponseEntity<AppResponse<?>> createLoan(@RequestBody LoanRequestDto requestDto) {

        LoanResponseDto response = loanService.createLoan(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AppResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/")
    public AppResponse<?> getLoanListPerUser(
            @RequestParam(required = false, value = "queryParameter") String queryParameter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "size", required = false) Integer size
            ) {

        page = page == null || page == 0 ? 1 : page;
        size = size == null || size == 0 ? 100 : size;

        Page<LoanResponseDto> response = loanService.getLoanListPerUser(queryParameter, startDate, endDate, page, size);

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

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{loanId}/update")
    public ResponseEntity<AppResponse<?>>updateLoanStatus(@PathVariable("loanId") String loanId,
                                           @RequestParam("status") String status) {

        // try rate limiting here
        if (bucket.tryConsume(1)) {
            LoanResponseDto response = loanService.updateLoanStatus(loanId, status);

            return ResponseEntity.status(HttpStatus.OK).body(
                    AppResponse.builder()
                            .statusCode(HttpStatus.OK.value())
                            .httpStatus(HttpStatus.OK)
                            .data(response)
                            .build()
            );
        }

        return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(
                AppResponse.builder()
                        .statusCode(HttpStatus.TOO_MANY_REQUESTS.value())
                        .httpStatus(HttpStatus.TOO_MANY_REQUESTS)
                        .build()
        );
    }
}
