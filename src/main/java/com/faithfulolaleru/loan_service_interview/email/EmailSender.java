package com.faithfulolaleru.loan_service_interview.email;

public interface EmailSender {

    void send(String to, String subject, String content);

    void sendTemplate(String to, String subject, String content);
}
