package com.example.jobportal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Service
public class OtpService {
    @Autowired
    private JavaMailSender mailSender;

    private static final String OTP_CHARS = "0123456789";
    private static final int OTP_LENGTH = 6;

    private Map<String, String> otpMap = new HashMap<>();

    public String generateOtp() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(OTP_CHARS.charAt(random.nextInt(OTP_CHARS.length())));
        }
        return otp.toString();
    }

    public void sendOtp(String recipientEmail, String otp) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject("OTP for Verification");
        message.setText("Your OTP is: " + otp);
        mailSender.send(message);
        otpMap.put(recipientEmail, otp);
    }

    public String getStoredOtp(String email) {
        return otpMap.get(email); // Retrieve stored OTP from map
    }
}
