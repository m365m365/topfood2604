package com.example.topfood2604.util;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static List<AiRestaurantInfo> parseRestaurantJson(String json) {
        try {
            String cleanJson = json
                    .replace("```json", "")
                    .replace("```", "")
                    .trim();

            int start = cleanJson.indexOf("[");
            int end = cleanJson.lastIndexOf("]");

            if (start >= 0 && end > start) {
                cleanJson = cleanJson.substring(start, end + 1);
            }

            cleanJson = cleanJson
                    .replace("})]", "\"}]")
                    .replace(")]", "]");

            return objectMapper.readValue(
                    cleanJson,
                    new TypeReference<List<AiRestaurantInfo>>() {}
            );

        } catch (Exception e) {
            throw new RuntimeException("JSON 解析失敗: " + e.getMessage(), e);
        }
    }
}