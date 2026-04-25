package com.example.topfood2604.util;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<AiRestaurantInfo> parseRestaurantJson(String json) {
        try {
            String cleaned = cleanJson(json);
            return objectMapper.readValue(cleaned, new TypeReference<List<AiRestaurantInfo>>() {});
        } catch (Exception e) {
            throw new RuntimeException("JSON 解析失敗: " + e.getMessage(), e);
        }
    }

    private static String cleanJson(String json) {
        if (json == null) {
            return "[]";
        }

        String cleaned = json.trim();

        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7).trim();
        }
        if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3).trim();
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3).trim();
        }

        return cleaned;
    }
}