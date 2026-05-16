package com.example.topfood2604.service;

import com.example.topfood2604.entity.MemberDailyLikeUsage;
import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.repository.MemberDailyLikeUsageRepository;
import com.example.topfood2604.repository.RecommendedRestaurantRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class LikeService {

    private final MemberDailyLikeUsageRepository usageRepository;
    private final RecommendedRestaurantRepository restaurantRepository;

    public LikeService(
            MemberDailyLikeUsageRepository usageRepository,
            RecommendedRestaurantRepository restaurantRepository
    ) {
        this.usageRepository = usageRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public int addLike(Long memberId, Long recommendId) {

        LocalDate today = LocalDate.now();

        MemberDailyLikeUsage usage =
                usageRepository.findByMemberIdAndLikeDate(memberId, today)
                        .orElseGet(() -> {
                            MemberDailyLikeUsage u = new MemberDailyLikeUsage();
                            u.setMemberId(memberId);
                            u.setLikeDate(today);
                            u.setLikeCount(0);
                            return u;
                        });

        if (usage.getLikeCount() >= 5) {
            throw new RuntimeException("今日按讚已達上限 5 次");
        }

        usage.setLikeCount(usage.getLikeCount() + 1);
        usageRepository.save(usage);

        RecommendedRestaurant restaurant =
                restaurantRepository.findById(recommendId)
                        .orElseThrow(() -> new RuntimeException("找不到推薦餐廳"));

        if (restaurant.getLikeCount() == null) {
            restaurant.setLikeCount(0);
        }

        restaurant.setLikeCount(restaurant.getLikeCount() + 1);
        restaurantRepository.save(restaurant);

        return restaurant.getLikeCount();
    }
}