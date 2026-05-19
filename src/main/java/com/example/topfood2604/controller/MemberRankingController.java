package com.example.topfood2604.controller;

import com.example.topfood2604.dto.MemberRankingDto;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberRankingController {

    private final JdbcTemplate jdbcTemplate;

    public MemberRankingController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @GetMapping("/api/member-ranking")
    public Map<String, Object> getRanking(
            @RequestParam(defaultValue = "recommend") String type,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        int offset = (page - 1) * size;

        String orderBy = switch (type) {
            case "like" -> "like_count DESC";
            case "recent" -> "latest_time DESC";
            default -> "recommend_count DESC";
        };

        String sql = """
        SELECT
            m.id AS member_id,

            SUBSTRING_INDEX(m.username, '@', 1) AS username,

            COUNT(rr.id) AS recommend_count,

            COALESCE(SUM(rr.like_count), 0) AS like_count,

            (
                SELECT rr2.restaurant_name
                FROM member_recommend_restaurant mrr2
                JOIN recommended_restaurant rr2
                    ON rr2.id = mrr2.restaurant_id
                WHERE mrr2.member_id = m.id
                  AND rr2.status = 'ACTIVE'
                ORDER BY rr2.created_at DESC
                LIMIT 1
            ) AS latest_restaurant,

            MAX(rr.created_at) AS latest_time

        FROM members m

        LEFT JOIN member_recommend_restaurant mrr
            ON mrr.member_id = m.id

        LEFT JOIN recommended_restaurant rr
            ON rr.id = mrr.restaurant_id
           AND rr.status = 'ACTIVE'

        GROUP BY m.id, m.username

        ORDER BY %s

        LIMIT ? OFFSET ?
        """.formatted(orderBy);

        List<MemberRankingDto> content = jdbcTemplate.query(
                sql,
                (rs, rowNum) -> new MemberRankingDto(
                        rs.getLong("member_id"),
                        rs.getString("username"),
                        rs.getLong("recommend_count"),
                        rs.getLong("like_count"),
                        rs.getString("latest_restaurant"),
                        rs.getString("latest_time")
                ),
                size,
                offset
        );

        String countSql = """
                SELECT COUNT(*) FROM (
                    SELECT m.id
                    FROM members m
                    JOIN member_recommend_restaurant mrr
                        ON mrr.member_id = m.id
                    JOIN recommended_restaurant rr
                        ON rr.id = mrr.restaurant_id
                    WHERE rr.status = 'ACTIVE'
                    GROUP BY m.id
                ) t
                """;

        Integer totalCount = jdbcTemplate.queryForObject(countSql, Integer.class);
        int totalPages = (int) Math.ceil((double) totalCount / size);

        Map<String, Object> result = new HashMap<>();
        result.put("content", content);
        result.put("totalPages", totalPages);

        return result;
    }
}