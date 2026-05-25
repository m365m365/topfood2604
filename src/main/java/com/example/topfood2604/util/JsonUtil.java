package com.example.topfood2604.util;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static List<AiRestaurantInfo> parseRestaurantJson(String json) {

        try {

            if (json == null || json.isBlank()) {
                return new ArrayList<>();
            }

            // 去除 markdown
            json = json.replace("```json", "")
                    .replace("```", "")
                    .trim();

            // 找到第一個 [
            int start = json.indexOf("[");

            // 找到最後一個 ]
            int end = json.lastIndexOf("]");

            if (start >= 0 && end >= 0) {
                json = json.substring(start, end + 1);
            }

            System.out.println("=== 修正後 JSON ===");
            System.out.println(json);

            return mapper.readValue(
                    json,
                    new TypeReference<List<AiRestaurantInfo>>() {}
            );

        } catch (Exception e) {

            System.out.println("=== JSON 解析失敗 ===");
            System.out.println(json);

            throw new RuntimeException(
                    "JSON 解析失敗: " + e.getMessage(),
                    e
            );
        }
    }
}