package com.example.topfood2604.controller;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.service.AiRestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/AiRestaurantApi")
@CrossOrigin
public class AiRestaurantController {

    private final AiRestaurantService aiRestaurantService;

    public AiRestaurantController(AiRestaurantService aiRestaurantService) {
        this.aiRestaurantService = aiRestaurantService;
    }

    @GetMapping("/test")
    public ResponseEntity<List<AiRestaurantInfo>> testAiSearch() {
        return ResponseEntity.ok(aiRestaurantService.getAiRestaurantsAndSave());
    }

    @GetMapping
    public ResponseEntity<List<AiRestaurantInfo>> getAll() {
        return ResponseEntity.ok(aiRestaurantService.findAll());
    }
}