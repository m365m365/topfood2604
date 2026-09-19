package com.example.topfood2604.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MaintenanceController {

    @GetMapping("/maintenance")
    public String maintenancePage() {
        return "maintenance";
    }
}