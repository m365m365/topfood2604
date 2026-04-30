package com.example.topfood2604.controller;

import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.repository.RecommendedRestaurantRepository;
import com.example.topfood2604.service.S3ImageService;
import com.example.topfood2604.service.S3ImageService.ImageResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/recommended-restaurants")
public class RecommendedRestaurantController {

    private final RecommendedRestaurantRepository repository;
    private final S3ImageService s3ImageService;

    public RecommendedRestaurantController(
            RecommendedRestaurantRepository repository,
            S3ImageService s3ImageService
    ) {
        this.repository = repository;
        this.s3ImageService = s3ImageService;
    }

    @PostMapping
    public RecommendedRestaurant create(
            @RequestParam String restaurantName,
            @RequestParam String address,
            @RequestParam Integer starRating,
            @RequestParam String description,
            @RequestParam MultipartFile photo
    ) throws Exception {

        RecommendedRestaurant restaurant = new RecommendedRestaurant();

        restaurant.setRestaurantName(restaurantName);
        restaurant.setAddress(address);
        restaurant.setStarRating(starRating);
        restaurant.setDescription(description);
        restaurant.setStatus("APPROVED");
        restaurant.setCreatedAt(LocalDateTime.now());

        String mapUrl = "https://www.google.com/maps/search/?api=1&query="
                + URLEncoder.encode(address, StandardCharsets.UTF_8);

        restaurant.setMapUrl(mapUrl);

        RecommendedRestaurant saved = repository.save(restaurant);

        ImageResult imageResult =
                s3ImageService.uploadRestaurantImage(saved.getId(), photo);

        saved.setImageUrl(imageResult.imageUrl());
        saved.setThumbUrl(imageResult.thumbUrl());

        return repository.save(saved);
    }

    @GetMapping("/home")
    public List<RecommendedRestaurant> home() {
        return repository.findTop6ByStatusOrderByStarRatingDescCreatedAtDesc("APPROVED");
    }
}