package com.example.topfood2604.controller;

import com.example.topfood2604.entity.RecommendedRestaurant;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MemberRestaurantPageController {

    private final JdbcTemplate jdbcTemplate;

    public MemberRestaurantPageController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/member/{id}/restaurants")
    public String memberRestaurants(
            @PathVariable Long id,
            Model model
    ) {
        String memberNameSql = """
                SELECT SUBSTRING_INDEX(username, '@', 1)
                FROM members
                WHERE id = ?
                """;

        String memberName = jdbcTemplate.queryForObject(
                memberNameSql,
                String.class,
                id
        );

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
                    rr.status,
                    rr.created_at,
                    rr.like_count
                FROM member_recommend_restaurant mrr
                JOIN recommended_restaurant rr
                    ON rr.id = mrr.restaurant_id
                WHERE mrr.member_id = ?
                  AND rr.status = 'ACTIVE'
                ORDER BY rr.created_at DESC
                """;

        List<RecommendedRestaurant> restaurants = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> {
                    RecommendedRestaurant r = new RecommendedRestaurant();
                    r.setId(rs.getLong("id"));
                    r.setRestaurantName(rs.getString("restaurant_name"));
                    r.setAddress(rs.getString("address"));
                    r.setImageUrl(rs.getString("image_url"));
                    r.setThumbUrl(rs.getString("thumb_url"));
                    r.setStarRating(rs.getInt("star_rating"));
                    r.setDescription(rs.getString("description"));
                    r.setMapUrl(rs.getString("map_url"));
                    r.setStatus(rs.getString("status"));
                    r.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                    r.setLikeCount(rs.getInt("like_count"));
                    return r;
                },
                id
        );

        model.addAttribute("memberName", memberName);
        model.addAttribute("restaurants", restaurants);

        return "member-restaurants";
    }
}