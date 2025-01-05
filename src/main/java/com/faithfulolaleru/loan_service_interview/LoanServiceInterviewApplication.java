package com.faithfulolaleru.loan_service_interview;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

// @EnableTransactionManagement
@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.faithfulolaleru.loan_service_interview.repository")
public class LoanServiceInterviewApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoanServiceInterviewApplication.class, args);
	}

}
