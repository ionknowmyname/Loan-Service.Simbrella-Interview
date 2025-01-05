package com.faithfulolaleru.loan_service_interview.utils;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaginationMetadata {
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
}
