package com.example.topfood2604.controller;

import com.example.topfood2604.service.AiRestaurantDetailService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/restaurants/ai")
public class AiRestaurantDetailController {

    private final AiRestaurantDetailService aiRestaurantDetailService;

    public AiRestaurantDetailController(AiRestaurantDetailService aiRestaurantDetailService) {
        this.aiRestaurantDetailService = aiRestaurantDetailService;
    }

    @GetMapping("/detail")
    public ResponseEntity<String> getRestaurantDetail(
            @RequestParam String name,
            @RequestParam String address
    ) {
        String result = aiRestaurantDetailService.generateDetail(name, address);
        return ResponseEntity.ok(result);
    }
}