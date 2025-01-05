package com.faithfulolaleru.loan_service_interview.email;

import com.faithfulolaleru.loan_service_interview.exception.ErrorResponse;
import com.faithfulolaleru.loan_service_interview.exception.GeneralException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
// @RequiredArgsConstructor
@AllArgsConstructor
public class EmailService implements EmailSender {

    private final JavaMailSender mailSender;


    @Override
    @Async
    public void send(String to, String subject, String content) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper =
                    new MimeMessageHelper(mimeMessage, true);  // multipart can be true // encoding can be "utf-8"
            helper.setText(content, "true");  // true for if we want it as html
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setFrom("Testing456@gmail.com");

            mailSender.send(mimeMessage);
        } catch (MessagingException ex) {
            log.error("failed to send email", ex);
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_EMAIL,
                    "Failed to send email");
        }

    }

    @Override
    @Async
    public void sendTemplate(String to, String subject, String content) {

        try {
            MimeMessage message = mailSender.createMimeMessage();

            message.setFrom(new InternetAddress("Testing456@gmail.com"));
            message.setRecipients(MimeMessage.RecipientType.TO, to);
            message.setSubject(subject);

            String htmlTemplate = readFile("emailTemplate.html");

            String htmlContent = htmlTemplate.replace("${name}", "Test Testing");
            htmlContent = htmlTemplate.replace("${content}", "Hello, this is a test email.");

            message.setContent(htmlContent, "text/html; charset=utf-8");

            mailSender.send(message);
        } catch (MessagingException | IOException ex) {
            log.error("failed to send email", ex);
            throw new GeneralException(HttpStatus.BAD_REQUEST, ErrorResponse.ERROR_EMAIL,
                    "Failed to send email");
        }

    }

    public String readFile(String filePath) throws IOException {
        Path path = Paths.get(filePath);
        return Files.readString(path, StandardCharsets.UTF_8);
    }
}
