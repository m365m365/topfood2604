package com.example.topfood2604.service;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.entity.RecommendedRestaurant;

import com.example.topfood2604.repository.MemberRecommendRestaurantRepository;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.RecommendedRestaurantRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RecommendRestaurantService {

    private final RecommendedRestaurantRepository recommendedRestaurantRepository;
    private final MemberRecommendRestaurantRepository memberRecommendRestaurantRepository;
    private final MemberRepository memberRepository;
    private final GooglePlacesService googlePlacesService;

    public RecommendRestaurantService(
            RecommendedRestaurantRepository recommendedRestaurantRepository,
            MemberRecommendRestaurantRepository memberRecommendRestaurantRepository,
            MemberRepository memberRepository,
            GooglePlacesService googlePlacesService
    ) {
        this.recommendedRestaurantRepository = recommendedRestaurantRepository;
        this.memberRecommendRestaurantRepository = memberRecommendRestaurantRepository;
        this.memberRepository = memberRepository;
        this.googlePlacesService = googlePlacesService;
    }

    @Transactional
    public RecommendedRestaurant saveTemp(
            RecommendedRestaurant restaurant
    ) {
        return recommendedRestaurantRepository.save(restaurant);
    }

    @Transactional
    public RecommendedRestaurant finishRecommendByUsername(
            String username,
            RecommendedRestaurant restaurant
    ) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("找不到會員：" + username)
                );

        RecommendedRestaurant savedRestaurant =
                recommendedRestaurantRepository.save(restaurant);

        MemberRecommendRestaurant relation =
                new MemberRecommendRestaurant();

        relation.setMember(member);
        relation.setRestaurant(savedRestaurant);

        memberRecommendRestaurantRepository.save(relation);

        return savedRestaurant;
    }

    @Transactional
    public RecommendedRestaurant createRecommendByUsername(
            String username,
            String restaurantName,
            String address,
            String imageUrl,
            String thumbUrl,
            Integer starRating,
            String description,
            String mapUrl
    ) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() ->
                        new RuntimeException("找不到會員：" + username)
                );

        RecommendedRestaurant restaurant = new RecommendedRestaurant();

        restaurant.setRestaurantName(restaurantName);
        restaurant.setAddress(address);
        restaurant.setImageUrl(imageUrl);
        restaurant.setThumbUrl(thumbUrl);
        restaurant.setStarRating(starRating);
        restaurant.setDescription(description);
        restaurant.setMapUrl(mapUrl);

        RecommendedRestaurant savedRestaurant =
                recommendedRestaurantRepository.save(restaurant);

        MemberRecommendRestaurant relation =
                new MemberRecommendRestaurant();

        relation.setMember(member);
        relation.setRestaurant(savedRestaurant);

        memberRecommendRestaurantRepository.save(relation);

        return savedRestaurant;
    }

    // 新增：重新查詢正確 Google 地圖
    public String findCorrectMapUrl(Long id) {

        RecommendedRestaurant restaurant =
                recommendedRestaurantRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException("找不到餐廳"));

        String name = restaurant.getRestaurantName();
        String address = restaurant.getAddress();

        String query = name + " " + address;

        try {
            return googlePlacesService.searchMapUrl(query);
        } catch (Exception e) {
            throw new RuntimeException("Google Places 地圖查詢失敗", e);
        }
    }
}