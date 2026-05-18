package com.example.topfood2604.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/index.html")
    public String homePage() {
        return "/index.html";
    }

    @GetMapping("/admin/dashboard")
    public String adminPage() {
        return "admin-dashboard";
    }
}