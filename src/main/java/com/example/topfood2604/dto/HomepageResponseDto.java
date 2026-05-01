package com.example.topfood2604.dto;

import com.example.topfood2604.entity.RecommendedRestaurant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HomepageResponseDto {

    private List<AiRestaurantResponseDto> aiRestaurants;

    private List<RecommendedRestaurant> recommendedRestaurants;
}