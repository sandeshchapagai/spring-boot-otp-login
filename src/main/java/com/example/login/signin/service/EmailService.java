package com.example.login.signin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    public void sendOtpEmail(String toEmail, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(toEmail);
            helper.setSubject("Your OTP Code");

            Context context = new Context();
            context.setVariable("otp", otp);

            String content = templateEngine.process("otp-email.html", context);
            helper.setText(content, true); // 'true' enables HTML content

            mailSender.send(message);
        } catch (MessagingException | MailException e) {
            System.err.println("Failed to send OTP email to " + toEmail + ": " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }
}
