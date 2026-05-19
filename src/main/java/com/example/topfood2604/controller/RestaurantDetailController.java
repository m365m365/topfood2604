package com.example.topfood2604.controller;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class RestaurantDetailController {

    private final JdbcTemplate jdbcTemplate;

    public RestaurantDetailController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/restaurant/{id}")
    public String detail(@PathVariable Long id, Model model) {

        String sql = """
                SELECT
                    rr.id,
                    rr.restaurant_name,
                    rr.address,
                    rr.image_url,
                    rr.thumb_url,
                    rr.star_rating,
                    rr.description,
                    rr.map_url,
                    rr.like_count,
                    m.id AS member_id,
                    SUBSTRING_INDEX(m.username, '@', 1) AS member_name
                FROM recommended_restaurant rr
                LEFT JOIN member_recommend_restaurant mrr
                    ON mrr.restaurant_id = rr.id
                LEFT JOIN members m
                    ON m.id = mrr.member_id
                WHERE rr.id = ?
                  AND rr.status = 'ACTIVE'
                LIMIT 1
                """;

        Map<String, Object> restaurant = jdbcTemplate.queryForMap(sql, id);

        model.addAttribute("restaurant", restaurant);

        return "restaurant-detail";
    }
}