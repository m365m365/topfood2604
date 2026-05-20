package com.example.topfood2604.service;

import org.springframework.stereotype.Service;

@Service
public class AiRestaurantDetailService {

    private final ChatGptService chatGptService;

    public AiRestaurantDetailService(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    public String generateDetail(String name, String address) {

        String prompt = """
                請依照以下餐廳名稱與地址，產生一份餐廳詳細介紹。
                
                餐廳名稱：%s
                餐廳地址：%s
                
                請用繁體中文回答。
                請包含：
                1. 餐廳簡介
                2. 可能的餐廳特色
                3. 推薦菜色
                4. 適合族群
                5. 交通與地點說明
                6. 消費建議
                
                請不要使用 Markdown。
                """.formatted(name, address);

        return chatGptService.ask(prompt);
    }
}