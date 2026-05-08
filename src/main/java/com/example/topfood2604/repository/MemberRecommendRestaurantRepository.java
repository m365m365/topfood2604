package com.example.topfood2604.repository;

import com.example.topfood2604.entity.MemberRecommendRestaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRecommendRestaurantRepository
        extends JpaRepository<MemberRecommendRestaurant, Long> {

    List<MemberRecommendRestaurant>
    findByMemberIdOrderByIdDesc(Long memberId);
}