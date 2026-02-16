package org.harshdev.ecom.service;

import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

//    public void sendOrderConfirmation(String toEmail, String orderId) {
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setFrom("harshraomy@gmail.com"); // Gemini: Ise jarur dalo
//        message.setTo(toEmail);
//        message.setSubject("Order Confirmation - " + orderId);
//        message.setText("Bhai, tera order " + orderId + " successfully place ho gaya hai.");
//        mailSender.send(message);
//    }

    public void sendOrderConfirmation(String toEmail, String orderId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom("harshraomy@gmail.com");
            helper.setTo(toEmail);
            helper.setSubject("Order Confirmation - " + orderId);

            String body = """
                    <h2>Order Confirmed 🎉</h2>
                    <p>Bhai, tera order <b>%s</b> successfully place ho gaya hai.</p>
                    <p>Thanks for shopping ❤️</p>
                    """.formatted(orderId);

            helper.setText(body, true);

            mailSender.send(message);

            System.out.println("✅ Email sent successfully to " + toEmail);

        } catch (Exception e) {
            System.out.println("❌ Email sending failed: " + e.getMessage());
            e.printStackTrace();
        }
    }
}