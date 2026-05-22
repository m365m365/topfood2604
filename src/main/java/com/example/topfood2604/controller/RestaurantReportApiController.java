package com.example.topfood2604.controller;

import com.example.topfood2604.dto.ReportResponseDto;
import com.example.topfood2604.service.RestaurantReportService;

import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/reports")
public class RestaurantReportApiController {

    private final RestaurantReportService reportService;

    public RestaurantReportApiController(
            RestaurantReportService reportService
    ) {
        this.reportService = reportService;
    }

    @PostMapping("/restaurants/{restaurantId}")
    public ReportResponseDto reportRestaurant(
            @PathVariable Long restaurantId,
            Principal principal
    ) {
        return reportService.reportRestaurant(restaurantId, principal);
    }
}