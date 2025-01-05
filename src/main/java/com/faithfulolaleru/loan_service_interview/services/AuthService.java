package com.faithfulolaleru.loan_service_interview.services;

import com.faithfulolaleru.loan_service_interview.config.jwt.JwtService;
import com.faithfulolaleru.loan_service_interview.dto.Credentials;
import com.faithfulolaleru.loan_service_interview.dto.LoginResponse;
import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
// @AllArgsConstructor
public record AuthService(JwtService jwtService, AuthenticationManager authenticationManager) {

    public LoginResponse loginUser(Credentials requestDto) {

        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    requestDto.getEmail(),
                    requestDto.getPassword()
                )
            );
        } catch (AuthenticationException ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                HttpStatus.BAD_REQUEST,
                ErrorResponse.ERROR_APP_USER,
                "Invalid User Credentials"
            );
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        return jwtService.generateToken(authentication);

    }
}
