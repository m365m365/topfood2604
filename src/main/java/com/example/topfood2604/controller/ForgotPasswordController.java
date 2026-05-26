package com.example.topfood2604.controller;

import com.example.topfood2604.service.PasswordResetService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ForgotPasswordController {

    private final PasswordResetService passwordResetService;

    public ForgotPasswordController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    // 1. 顯示忘記密碼頁
    @GetMapping("/forgot-password")
    public String forgotPasswordPage() {
        return "forgot-password";
    }

    // 2. 送出 Email，產生 token，寄信
    @PostMapping("/forgot-password")
    public String sendResetPasswordEmail(
            @RequestParam String email,
            Model model
    ) {
        passwordResetService.sendResetPasswordEmail(email);

        model.addAttribute(
                "message",
                "如果此 Email 已註冊，我們已寄出重設密碼信。"
        );

        return "forgot-password";
    }

    // 3. 顯示重設密碼頁
    @GetMapping("/reset-password")
    public String resetPasswordPage(
            @RequestParam String token,
            Model model
    ) {
        boolean valid = passwordResetService.isValidToken(token);

        if (!valid) {
            model.addAttribute("error", "重設密碼連結無效或已過期");
            return "reset-password-error";
        }

        model.addAttribute("token", token);
        return "reset-password";
    }

    // 4. 更新新密碼
    @PostMapping("/reset-password")
    public String resetPassword(
            @RequestParam String token,
            @RequestParam String password,
            @RequestParam String confirmPassword,
            Model model
    ) {
        if (!password.equals(confirmPassword)) {
            model.addAttribute("token", token);
            model.addAttribute("error", "兩次輸入的密碼不一致");
            return "reset-password";
        }

        passwordResetService.resetPassword(token, password);

        model.addAttribute("message", "密碼重設成功，請重新登入");
        return "login";
    }
}