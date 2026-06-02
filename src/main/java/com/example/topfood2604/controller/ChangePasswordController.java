package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.repository.MemberRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class ChangePasswordController {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public ChangePasswordController(
            MemberRepository memberRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/change-password")
    public String changePassword(
            Authentication authentication,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            RedirectAttributes redirectAttributes
    ) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        String username = authentication.getName();

        Member member = memberRepository
                .findByUsername(username)
                .orElse(null);

        if (member == null) {
            return "redirect:/login";
        }

        if (!passwordEncoder.matches(oldPassword, member.getPassword())) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "目前密碼不正確"
            );

            return "redirect:/member-center#change";
        }

        if (!newPassword.equals(confirmPassword)) {

            redirectAttributes.addFlashAttribute(
                    "error",
                    "兩次新密碼輸入不一致"
            );

            return "redirect:/member-center#change";
        }

        member.setPassword(
                passwordEncoder.encode(newPassword)
        );

        memberRepository.save(member);

        redirectAttributes.addFlashAttribute(
                "success",
                "密碼修改成功，系統將在 3 秒後自動登出，請重新登入。"
        );

        redirectAttributes.addFlashAttribute(
                "forceLogout",
                true
        );

        return "redirect:/member-center#change";
    }
}