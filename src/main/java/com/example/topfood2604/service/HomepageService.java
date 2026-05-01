package com.example.topfood2604.service;

import com.example.topfood2604.dto.AiRestaurantResponseDto;
import com.example.topfood2604.dto.HomepageResponseDto;
import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.repository.AiRestaurantRepository;
import com.example.topfood2604.repository.RecommendedRestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HomepageService {

    private final AiRestaurantRepository aiRestaurantRepository;
    private final RecommendedRestaurantRepository recommendedRestaurantRepository;

    public HomepageService(
            AiRestaurantRepository aiRestaurantRepository,
            RecommendedRestaurantRepository recommendedRestaurantRepository
    ) {
        this.aiRestaurantRepository = aiRestaurantRepository;
        this.recommendedRestaurantRepository = recommendedRestaurantRepository;
    }

    public HomepageResponseDto getHomepageData() {

        List<AiRestaurantResponseDto> aiList =
                aiRestaurantRepository.findTop6ByOrderByIdDesc()
                        .stream()
                        .map(this::toAiDto)
                        .toList();

        List<RecommendedRestaurant> recommendList =
                recommendedRestaurantRepository.findTop6ByOrderByIdDesc();

        return new HomepageResponseDto(aiList, recommendList);
    }

    private AiRestaurantResponseDto toAiDto(AiRestaurantInfo r) {
        return new AiRestaurantResponseDto(
                r.getName(),
                r.getUrl(),
                r.getImageUrl(),
                r.getAddress()
        );
    }
}