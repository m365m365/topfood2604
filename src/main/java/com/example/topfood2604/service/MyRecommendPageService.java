package com.example.topfood2604.service;

import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.repository.MemberRecommendRestaurantRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MyRecommendPageService {

    private final MemberRecommendRestaurantRepository repository;

    public MyRecommendPageService(
            MemberRecommendRestaurantRepository repository
    ) {
        this.repository = repository;
    }

    public List<MemberRecommendRestaurant> getMyRestaurants(Long memberId) {
        return repository.findByMemberIdOrderByIdDesc(memberId);
    }

    public MemberRecommendRestaurant getRecommendDetail(Long relationId) {
        return repository.findById(relationId)
                .orElse(null);
    }
}