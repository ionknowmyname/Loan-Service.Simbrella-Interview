package com.faithfulolaleru.loan_service_interview.dto;

import lombok.*;

import java.time.Instant;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    private String accessToken;
    private String refreshToken;
    private Instant expiresIn;
}
