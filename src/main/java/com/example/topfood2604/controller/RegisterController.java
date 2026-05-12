package com.example.topfood2604.controller;

import com.example.topfood2604.dto.RegisterRequestDto;
import com.example.topfood2604.service.RegisterService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        this.registerService = registerService;
    }

    @GetMapping("/register")
    public String registerPage(Model model) {
        model.addAttribute("registerRequestDto", new RegisterRequestDto());
        return "register";
    }

    @PostMapping("/register")
    public String register(
            @Valid @ModelAttribute RegisterRequestDto dto,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            return "register";
        }

        try {
            registerService.register(dto);
            return "email-confirm";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "register";
        }
    }

    @GetMapping("/verify-email")
    public String verifyEmail(@RequestParam String token, Model model) {
        try {
            registerService.verifyEmail(token);
            model.addAttribute("successMessage", "Email 驗證成功，現在可以登入了。");
            return "verify-success";
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "verify-failed";
        }
    }
}