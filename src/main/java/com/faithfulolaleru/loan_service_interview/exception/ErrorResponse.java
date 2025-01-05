package com.faithfulolaleru.loan_service_interview.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {


    public static final String ERROR_APP_USER = "APP_USER_ERROR_ENCOUNTERED";

    public static final String ERROR_EMAIL = "EMAIL_ERROR_ENCOUNTERED";

    public static final String ERROR_LOAN = "LOAN_ERROR_ENCOUNTERED";

    public static final String ERROR_TRANSACTION = "TRANSACTION_ERROR_ENCOUNTERED";



    private String error;
    private String message;
    private HttpStatus httpStatus;


    public ErrorResponse(final GeneralException ex) {
        this.httpStatus = ex.getHttpStatus();
        this.error = ex.getError();
        this.message = ex.getMessage();   //  ex.getLocalizedMessage()
    }
}
