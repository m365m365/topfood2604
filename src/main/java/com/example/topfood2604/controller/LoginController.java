package com.example.topfood2604.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/")
    public String homePage() {
        return "adminok";
    }

    @GetMapping("/admin/dashboard")
    public String adminPage() {
        return "admin-dashboard";
    }
}