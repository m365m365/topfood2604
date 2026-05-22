package com.example.topfood2604.controller;

import com.example.topfood2604.repository.RestaurantReportRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminReportController {

    private final RestaurantReportRepository reportRepository;

    public AdminReportController(
            RestaurantReportRepository reportRepository
    ) {
        this.reportRepository = reportRepository;
    }

    @GetMapping("/admin/reports")
    public String reports(Model model) {

        model.addAttribute(
                "reports",
                reportRepository.findAllByOrderByCreatedAtDesc()
        );

        return "admin/reports";
    }
}