package com.example.topfood2604.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
public class RestaurantLikeApiController {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantLikeApiController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostMapping("/api/restaurant/{id}/like")
    public Map<String, Object> like(
            @PathVariable Long id,
            Authentication authentication
    ) {
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            return Map.of(
                    "success", false,
                    "message", "請先登入後才能按讚"
            );
        }

        String username = authentication.getName();

        Long memberId = jdbcTemplate.queryForObject(
                "SELECT id FROM members WHERE username = ?",
                Long.class,
                username
        );

        LocalDate today = LocalDate.now();

        Integer todayLikeCount = jdbcTemplate.queryForObject("""
                SELECT COUNT(*)
                FROM member_daily_like_usage
                WHERE member_id = ?
                  AND like_date = ?
                """, Integer.class, memberId, today);

        if (todayLikeCount == 0) {
            jdbcTemplate.update("""
                    INSERT INTO member_daily_like_usage
                    (member_id, like_date, like_count)
                    VALUES (?, ?, 0)
                    """, memberId, today);
        }

        Integer usedCount = jdbcTemplate.queryForObject("""
                SELECT like_count
                FROM member_daily_like_usage
                WHERE member_id = ?
                  AND like_date = ?
                """, Integer.class, memberId, today);

        if (usedCount >= 5) {
            return Map.of(
                    "success", false,
                    "message", "今日按讚已達 5 次上限"
            );
        }

        jdbcTemplate.update("""
                UPDATE recommended_restaurant
                SET like_count = COALESCE(like_count, 0) + 1
                WHERE id = ?
                  AND status = 'ACTIVE'
                """, id);

        jdbcTemplate.update("""
                UPDATE member_daily_like_usage
                SET like_count = like_count + 1
                WHERE member_id = ?
                  AND like_date = ?
                """, memberId, today);

        Integer likeCount = jdbcTemplate.queryForObject("""
                SELECT like_count
                FROM recommended_restaurant
                WHERE id = ?
                """, Integer.class, id);

        Integer remaining = 5 - usedCount - 1;

        return Map.of(
                "success", true,
                "likeCount", likeCount,
                "remaining", remaining,
                "message", "按讚成功，今日還可按 " + remaining + " 次"
        );
    }
}