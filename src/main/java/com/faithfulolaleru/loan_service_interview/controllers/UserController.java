package com.faithfulolaleru.loan_service_interview.controllers;

import com.faithfulolaleru.loan_service_interview.dto.UserCreateDto;
import com.faithfulolaleru.loan_service_interview.dto.UserResponseDto;
import com.faithfulolaleru.loan_service_interview.services.UserService;
import com.faithfulolaleru.loan_service_interview.utils.AppResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/")
    public ResponseEntity<AppResponse<?>> registerUser(@RequestBody UserCreateDto requestDto) {

        UserResponseDto response = userService.registerUser(requestDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(
                AppResponse.builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .httpStatus(HttpStatus.CREATED)
                        .data(response)
                        .build()
        );
    }

}
