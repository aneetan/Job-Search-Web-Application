package com.example.jobportal.service;

import com.example.jobportal.Configuration.MailConfig;
import com.example.jobportal.model.MailStructure;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private MailConfig mailConfig;

    @Value("${spring.mail.username")
    private String fromMail;

    public void sendMail(String mail, MailStructure mailStructure){
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom(fromMail);
        simpleMailMessage.setSubject(mailStructure.getSubject());
        simpleMailMessage.setText(mailStructure.getMessage());
        simpleMailMessage.setTo(mail);

        mailSender.send(simpleMailMessage);
    }

    public void sendApprovalEmail(String to, String username, String jobName, String companyName) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(mailConfig.getSmtpUsername());
            helper.setTo(to);
            helper.setSubject("Regarding Your Job Application");

            String messageBody = "Dear " + username + ",\n\n" +
                    "We are pleased to inform you that your application for the position of " + jobName +
                    " at " + companyName + " has been approved for further consideration. Congratulations!\n\n" +
                    "We will be scheduling an interview for you in the upcoming days. We will notify you of the interview date and time shortly.\n\n" +
                    "Warm regards,\n" +
                    "JobSearch";

            helper.setText(messageBody);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }



    public void sendRejectionEmail(String to, String username, String jobName, String companyName) {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        try {
            helper.setFrom(mailConfig.getSmtpUsername());
            helper.setTo(to);
            helper.setSubject("Regarding Your Job Application");

            String messageBody = "Dear " + username + ",\n\n" +
                    "We regret to inform you that your application for the position of " + jobName +
                    " at " + companyName + " has not been successful. We appreciate the time and effort you put into your application.\n\n" +
                    "Unfortunately, after careful consideration, we have decided to proceed with other candidates whose qualifications more closely align with our requirements.\n\n" +
                    "We wish you the best of luck in your job search and thank you for considering us as a potential employer.\n\n" +
                    "Warm regards,\n" +
                    "JobSearch";

            helper.setText(messageBody);

            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace(); // Handle the exception properly
        }
    }
}
