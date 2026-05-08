package com.example.topfood2604.controller;

import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.service.RecommendRestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/recommended-restaurants")
public class RecommendedRestaurantController {

    private final RecommendRestaurantService recommendRestaurantService;

    public RecommendedRestaurantController(
            RecommendRestaurantService recommendRestaurantService
    ) {
        this.recommendRestaurantService = recommendRestaurantService;
    }

    @PostMapping
    public ResponseEntity<RecommendedRestaurant> createRecommend(

            @RequestParam String name,

            @RequestParam String address,

            @RequestParam Integer rating,

            @RequestParam String description,

            @RequestParam MultipartFile photo

    ) throws Exception {

        Long memberId = 1L;

        RecommendedRestaurant restaurant =
                new RecommendedRestaurant();

        restaurant.setRestaurantName(name);

        restaurant.setAddress(address);

        restaurant.setStarRating(rating);

        restaurant.setDescription(description);

        RecommendedRestaurant saved =
                recommendRestaurantService.createRecommend(
                        memberId,
                        restaurant,
                        photo
                );

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/home")
    public ResponseEntity<?> home() {

        return ResponseEntity.ok(
                recommendRestaurantService.home()
        );
    }
}