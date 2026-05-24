package com.example.topfood2604.controller;

import com.example.topfood2604.dto.ReportResponseDto;
import com.example.topfood2604.dto.ReportStatusDto;
import com.example.topfood2604.service.RestaurantReportService;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

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

    @GetMapping("/status")
    public Map<String, Object> reportStatus(Authentication authentication) {

        ReportStatusDto status =
                reportService.getTodayReportStatus(authentication);

        return Map.of(
                "canReport", status.isCanReport(),
                "used", status.getUsed(),
                "limit", status.getLimit(),
                "globalUsed", status.getGlobalUsed(),
                "globalLimit", status.getGlobalLimit()
        );
    }
}