package com.example.topfood2604.service;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetService {

    private final MemberRepository memberRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    public PasswordResetService(
            MemberRepository memberRepository,
            MailService mailService,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    // 送出忘記密碼信
    public void sendResetPasswordEmail(String email) {

        Optional<Member> optionalMember = memberRepository.findByEmail(email);

        // 安全設計：email 不存在也不報錯
        if (optionalMember.isEmpty()) {
            return;
        }

        Member member = optionalMember.get();

        String token = UUID.randomUUID().toString();

        member.setResetPasswordToken(token);
        member.setResetPasswordTokenExpireAt(LocalDateTime.now().plusMinutes(30));

        memberRepository.save(member);

        String resetLink = "https://topfoodai.com/reset-password?token=" + token;

        String subject = "TopFood AI 重設密碼通知";

        String content =
                "您好，\n\n" +
                        "請點擊以下連結重設您的 TopFood AI 密碼：\n\n" +
                        resetLink + "\n\n" +
                        "此連結 30 分鐘內有效。\n\n" +
                        "如果不是您本人操作，請忽略此信。\n\n" +
                        "TopFood AI";

        mailService.sendMail(email, subject, content);
    }

    // 檢查 token 是否有效
    public boolean isValidToken(String token) {

        Optional<Member> optionalMember =
                memberRepository.findByResetPasswordToken(token);

        if (optionalMember.isEmpty()) {
            return false;
        }

        Member member = optionalMember.get();

        if (member.getResetPasswordTokenExpireAt() == null) {
            return false;
        }

        return member.getResetPasswordTokenExpireAt()
                .isAfter(LocalDateTime.now());
    }

    // 重設密碼
    public void resetPassword(String token, String newPassword) {

        Member member = memberRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new RuntimeException("重設密碼連結無效"));

        if (member.getResetPasswordTokenExpireAt() == null ||
                member.getResetPasswordTokenExpireAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("重設密碼連結已過期");
        }

        String encodedPassword = passwordEncoder.encode(newPassword);

        member.setPassword(encodedPassword);

        // token 使用完要清除
        member.setResetPasswordToken(null);
        member.setResetPasswordTokenExpireAt(null);

        memberRepository.save(member);
    }
}