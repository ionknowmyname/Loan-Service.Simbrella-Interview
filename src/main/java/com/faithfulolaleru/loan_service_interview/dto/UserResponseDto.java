package com.faithfulolaleru.loan_service_interview.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserResponseDto {

    private String id;
    private String name;
    private String email;
    private String phoneNumber;
    private String userRole;
    private LocalDateTime createdAt;
}
