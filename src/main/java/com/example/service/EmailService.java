package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.example.model.User;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class EmailService {


		
    @Autowired
    private JavaMailSender emailSender;

    public void sendPasswordResetEmail(User user, String resetLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(user.getEmail());
        message.setSubject("Password Reset Request");
        message.setText("Dear " + user.getFullName() + ",\n\n"
                + "You have requested to reset your password. Please click on the link below to reset your password:\n\n"
                + resetLink + "\n\n"
                + "If you did not request this, please ignore this email. Your password will remain unchanged.\n\n"
                + "Regards,\nThe Ride Rental Team");

        emailSender.send(message);
    }
}
