package com.example.topfood2604.service;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.repository.AiRestaurantRepository;
import com.example.topfood2604.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AiRestaurantService {

    private final ChatGptService chatGptService;
    private final AiRestaurantRepository aiRestaurantRepository;

    private static final String PROMPT =
            "請列出台北16家熱門美食餐廳，只回傳 JSON 陣列，不要加任何說明文字，不要加 markdown。"
                    + "每筆包含 name 和 url。"
                    + "格式範例：["
                    + "{\"name\":\"鼎泰豐\",\"url\":\"https://www.dintaifung.com.tw/\"},"
                    + "{\"name\":\"阜杭豆漿\",\"url\":\"https://example.com\"}"
                    + "]";

    public AiRestaurantService(ChatGptService chatGptService,
                               AiRestaurantRepository aiRestaurantRepository) {
        this.chatGptService = chatGptService;
        this.aiRestaurantRepository = aiRestaurantRepository;
    }

    public List<AiRestaurantInfo> getAiRestaurantsAndSave() {
        String result = chatGptService.ask(PROMPT);

        System.out.println("=== ChatGPT 原始回傳 ===");
        System.out.println(result);

        List<AiRestaurantInfo> list = JsonUtil.parseRestaurantJson(result);

        for (AiRestaurantInfo item : list) {
            item.setId(null);

            if (item.getState() == null || item.getState().isBlank()) {
                item.setState("ACTIVE");
            }
        }

        return aiRestaurantRepository.saveAll(list);
    }

    public List<AiRestaurantInfo> findAll() {
        return aiRestaurantRepository.findAll();
    }
}