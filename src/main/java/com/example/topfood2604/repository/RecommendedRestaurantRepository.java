package com.example.topfood2604.repository;

import com.example.topfood2604.entity.RecommendedRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface RecommendedRestaurantRepository
        extends JpaRepository<RecommendedRestaurant, Long> {

    List<RecommendedRestaurant> findTop6ByStatusOrderByStarRatingDescCreatedAtDesc(String status);
    List<RecommendedRestaurant> findTop6ByOrderByIdDesc();

    Page<RecommendedRestaurant>
    findAllByOrderByIdDesc(Pageable pageable);
}