package com.example.topfood2604.repository;

import com.example.topfood2604.entity.AiRestaurantInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AiRestaurantRepository extends JpaRepository<AiRestaurantInfo, Long> {

    List<AiRestaurantInfo> findTop6ByOrderByIdDesc();
}