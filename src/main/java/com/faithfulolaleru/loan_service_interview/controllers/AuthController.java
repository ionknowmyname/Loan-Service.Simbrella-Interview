package com.faithfulolaleru.loan_service_interview.controllers;


import com.faithfulolaleru.loan_service_interview.dto.Credentials;
import com.faithfulolaleru.loan_service_interview.dto.LoginResponse;
import com.faithfulolaleru.loan_service_interview.services.AuthService;
import com.faithfulolaleru.loan_service_interview.utils.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public AppResponse<?> loginUser(@RequestBody Credentials requestDto) {

        LoginResponse response = authService.loginUser(requestDto);

        return AppResponse.builder()
                .statusCode(HttpStatus.OK.value())
                .httpStatus(HttpStatus.OK)
                .data(response)
                .build();
    }

}
