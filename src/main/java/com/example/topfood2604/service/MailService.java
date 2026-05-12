package com.example.topfood2604.service;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class MailService {

    private final JavaMailSender mailSender;

    public MailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendVerifyEmail(String to, String verifyUrl) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("TopFood AI 會員 Email 驗證");
        message.setText(
                "歡迎註冊 TopFood AI！\n\n" +
                        "請點擊以下連結完成 Email 驗證：\n" +
                        verifyUrl + "\n\n" +
                        "如果這不是你本人操作，請忽略此信。"
        );

        mailSender.send(message);
    }
}