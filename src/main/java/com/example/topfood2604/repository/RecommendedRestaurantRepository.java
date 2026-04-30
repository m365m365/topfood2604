package com.example.topfood2604.repository;

import com.example.topfood2604.entity.RecommendedRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendedRestaurantRepository
        extends JpaRepository<RecommendedRestaurant, Long> {

    List<RecommendedRestaurant> findTop6ByStatusOrderByStarRatingDescCreatedAtDesc(String status);
}