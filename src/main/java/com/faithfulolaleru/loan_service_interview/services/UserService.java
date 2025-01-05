package com.faithfulolaleru.loan_service_interview.services;


import com.faithfulolaleru.loan_service_interview.dto.UserCreateDto;
import com.faithfulolaleru.loan_service_interview.dto.UserResponseDto;
import com.faithfulolaleru.loan_service_interview.entity.User;
import com.faithfulolaleru.loan_service_interview.enums.UserRole;
import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
import com.faithfulolaleru.loan_service_interview.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_APP_USER,
                        "Invalid User Credentials"));

        return foundUser;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_APP_USER,
                        "Invalid User Credentials"
                ));

    }

    public User findUserById(String id) {
        return userRepository.findById(UUID.fromString(id))
                .orElseThrow(() -> new GeneralException(
                        HttpStatus.BAD_REQUEST,
                        ErrorResponse.ERROR_APP_USER,
                        "Invalid User Credentials"
                ));

    }

    public UserResponseDto registerUser(UserCreateDto dto) {
        // check if user exists by email, if exists, throw 400 bad request

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        User savedUser;
        try {
            savedUser = userRepository.save(buildUser(dto));
        } catch (Exception ex) {
            log.error(ex.getMessage());
            ex.printStackTrace();

            throw new GeneralException(
                    HttpStatus.BAD_REQUEST,
                    ErrorResponse.ERROR_APP_USER,
                    "User Registration Failed"
            );
        }

        return modelMapper.map(savedUser, UserResponseDto.class);
    }

    private User buildUser(UserCreateDto dto) {
        return User.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhoneNumber())
                .userRole(UserRole.valueOf(dto.getUserRole()))
                .password(dto.getPassword())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .currentLoanAmount(0.0)
                .maxLoanAmount(100000.0)
                .build();
    }

    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        return findUserByEmail(email);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }


}
