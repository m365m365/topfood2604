package com.example.topfood2604.service;

import com.example.topfood2604.dto.RegisterRequestDto;
import com.example.topfood2604.entity.Member;
import com.example.topfood2604.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class RegisterService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Value("${app.base-url}")
    private String baseUrl;

    public RegisterService(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder,
            MailService mailService
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
        this.mailService = mailService;
    }

    public void register(RegisterRequestDto dto) {

        if (!dto.getPassword().equals(dto.getConfirmPassword())) {
            throw new RuntimeException("兩次輸入的密碼不一致");
        }

        if (memberRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("此 Email 已經註冊");
        }

        String token = UUID.randomUUID().toString();

        Member member = new Member();
        member.setEmail(dto.getEmail());
        member.setUsername(dto.getEmail());
        member.setName(dto.getName());
        member.setPassword(passwordEncoder.encode(dto.getPassword()));
        member.setRole("ROLE_USER");
        member.setState("ACTIVE");
        member.setEmailVerified(false);
        member.setVerificationToken(token);
        member.setVerificationTokenExpiredAt(LocalDateTime.now().plusHours(24));

        memberRepository.save(member);

        System.out.println("開始寄送驗證信");

        String verifyUrl = baseUrl + "/verify-email?token=" + token;

        try {

            mailService.sendVerifyEmail(dto.getEmail(), verifyUrl);

            System.out.println("驗證信寄送成功");

        } catch (Exception e) {

            e.printStackTrace();

            System.out.println("寄信失敗：" + e.getMessage());
        }
    }

    public void verifyEmail(String token) {

        Member member = memberRepository.findByVerificationToken(token)
                .orElseThrow(() -> new RuntimeException("驗證連結無效"));

        if (member.getVerificationTokenExpiredAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("驗證連結已過期");
        }

        member.setEmailVerified(true);
        member.setVerificationToken(null);
        member.setVerificationTokenExpiredAt(null);

        memberRepository.save(member);
    }
}