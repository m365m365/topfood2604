package com.example.topfood2604.controller;

import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.service.RecommendRestaurantService;
import com.example.topfood2604.service.S3ImageService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

@RestController
@RequestMapping("/api/recommended-restaurants")
@CrossOrigin
public class RecommendedRestaurantController {

    private final RecommendRestaurantService recommendRestaurantService;

    private final S3ImageService s3ImageService;

    public RecommendedRestaurantController(
            RecommendRestaurantService recommendRestaurantService,
            S3ImageService s3ImageService
    ) {

        this.recommendRestaurantService =
                recommendRestaurantService;

        this.s3ImageService =
                s3ImageService;
    }

    @PostMapping
    public ResponseEntity<RecommendedRestaurant>
    createRecommend(

            @RequestParam String name,

            @RequestParam String address,

            @RequestParam Integer rating,

            @RequestParam String description,

            @RequestParam MultipartFile photo,

            Principal principal

    ) throws Exception {

        // =========================
        // 1. 檢查登入
        // =========================

        if (principal == null) {

            throw new RuntimeException("請先登入");
        }

        String username = principal.getName();

        // =========================
        // 2. 建立暫時餐廳
        // =========================

        RecommendedRestaurant tempRestaurant =
                new RecommendedRestaurant();

        tempRestaurant.setRestaurantName(name);

        tempRestaurant.setAddress(address);

        tempRestaurant.setStarRating(rating);

        tempRestaurant.setDescription(description);

        // 先存入 DB 拿到 ID
        RecommendedRestaurant savedTemp =

                recommendRestaurantService
                        .saveTemp(tempRestaurant);

        // =========================
        // 3. 上傳圖片到 S3
        // =========================

        S3ImageService.ImageResult imageResult =

                s3ImageService.uploadRestaurantImage(
                        savedTemp.getId(),
                        photo
                );

        // =========================
        // 4. 更新圖片 URL
        // =========================

        savedTemp.setImageUrl(
                imageResult.imageUrl()
        );

        savedTemp.setThumbUrl(
                imageResult.thumbUrl()
        );

        savedTemp.setMapUrl(
                "https://www.google.com/maps/search/?api=1&query="
                        + address
        );

        // =========================
        // 5. 建立會員關聯
        // =========================

        RecommendedRestaurant finalRestaurant =

                recommendRestaurantService
                        .finishRecommendByUsername(
                                username,
                                savedTemp
                        );

        // =========================
        // 6. 回傳
        // =========================

        return ResponseEntity.ok(finalRestaurant);
    }
}