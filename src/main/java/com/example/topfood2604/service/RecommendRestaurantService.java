package com.example.topfood2604.service;

import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.repository.MemberRecommendRestaurantRepository;
import com.example.topfood2604.repository.RecommendedRestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RecommendRestaurantService {

    private final RecommendedRestaurantRepository recommendedRestaurantRepository;

    private final MemberRecommendRestaurantRepository memberRecommendRestaurantRepository;

    private final S3ImageService s3ImageService;

    public RecommendRestaurantService(
            RecommendedRestaurantRepository recommendedRestaurantRepository,
            MemberRecommendRestaurantRepository memberRecommendRestaurantRepository,
            S3ImageService s3ImageService
    ) {
        this.recommendedRestaurantRepository =
                recommendedRestaurantRepository;

        this.memberRecommendRestaurantRepository =
                memberRecommendRestaurantRepository;

        this.s3ImageService =
                s3ImageService;
    }

    @Transactional
    public RecommendedRestaurant createRecommend(

            Long memberId,

            RecommendedRestaurant restaurant,

            MultipartFile photo

    ) throws Exception {

        // 建立時間
        restaurant.setCreatedAt(LocalDateTime.now());

        // 狀態
        restaurant.setStatus("APPROVED");

        // map url
        String mapUrl =
                "https://www.google.com/maps/search/?api=1&query="
                        + URLEncoder.encode(
                        restaurant.getAddress(),
                        StandardCharsets.UTF_8
                );

        restaurant.setMapUrl(mapUrl);

        // 1. 先存餐廳
        RecommendedRestaurant savedRestaurant =
                recommendedRestaurantRepository.save(
                        restaurant
                );

        // 2. 上傳圖片
        S3ImageService.ImageResult imageResult =
                s3ImageService.uploadRestaurantImage(
                        savedRestaurant.getId(),
                        photo
                );

        // 3. 更新圖片
        savedRestaurant.setImageUrl(
                imageResult.imageUrl()
        );

        savedRestaurant.setThumbUrl(
                imageResult.thumbUrl()
        );

        savedRestaurant =
                recommendedRestaurantRepository.save(
                        savedRestaurant
                );

        // 4. 建立會員關連
        MemberRecommendRestaurant relation =
                new MemberRecommendRestaurant();

        relation.setMemberId(memberId);

        relation.setRestaurantId(
                savedRestaurant.getId()
        );

        relation.setCreatedAt(LocalDateTime.now());

        memberRecommendRestaurantRepository.save(
                relation
        );

        return savedRestaurant;
    }

    public List<RecommendedRestaurant> home() {

        return recommendedRestaurantRepository
                .findTop6ByStatusOrderByStarRatingDescCreatedAtDesc(
                        "APPROVED"
                );
    }
}