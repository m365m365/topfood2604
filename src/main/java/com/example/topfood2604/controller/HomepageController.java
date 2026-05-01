package com.example.topfood2604.controller;

import com.example.topfood2604.dto.HomepageResponseDto;
import com.example.topfood2604.service.HomepageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomepageController {

    private final HomepageService homepageService;

    public HomepageController(HomepageService homepageService) {
        this.homepageService = homepageService;
    }

    @GetMapping("/api/homepage")
    public HomepageResponseDto getHomepage() {
        return homepageService.getHomepageData();
    }
}