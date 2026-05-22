package com.example.topfood2604.repository;

import com.example.topfood2604.entity.RestaurantReport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RestaurantReportRepository
        extends JpaRepository<RestaurantReport, Long> {

    Optional<RestaurantReport>
    findTopByRestaurantIdAndStatusOrderByIdDesc(
            Long restaurantId,
            String status
    );

    List<RestaurantReport>
    findAllByOrderByCreatedAtDesc();
}