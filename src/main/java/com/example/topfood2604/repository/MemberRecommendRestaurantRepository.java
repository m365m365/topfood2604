package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberRecommendRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRecommendRestaurantRepository
        extends JpaRepository<MemberRecommendRestaurant, Long> {

    List<MemberRecommendRestaurant>
    findByMemberIdOrderByIdDesc(Long memberId);

    Optional<MemberRecommendRestaurant>
    findByRestaurant_Id(Long restaurantId);
}