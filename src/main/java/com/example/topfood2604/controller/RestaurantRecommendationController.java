package com.example.topfood2604.controller;

import com.example.topfood2604.dto.AiRestaurantResponseDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/restaurants/recommendations")
public class RestaurantRecommendationController {

    @GetMapping("/ai")
    public List<AiRestaurantResponseDto> getAiRecommendations() {
        return List.of(
                new AiRestaurantResponseDto(
                        "鼎泰豐",
                        "https://www.dintaifung.com.tw/",
                        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?auto=format&fit=crop&w=800&q=80",
                        "台北市信義區松高路12號"
                ),
                new AiRestaurantResponseDto(
                        "阜杭豆漿",
                        "https://www.google.com/search?q=阜杭豆漿",
                        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?auto=format&fit=crop&w=800&q=80",
                        "台北市中正區忠孝東路一段108號"
                ),
                new AiRestaurantResponseDto(
                        "阿宗麵線",
                        "https://www.google.com/search?q=阿宗麵線",
                        "https://images.unsplash.com/photo-1526318896980-cf78c088247c?auto=format&fit=crop&w=800&q=80",
                        "台北市萬華區峨眉街8-1號"
                ),
                new AiRestaurantResponseDto(
                        "永康牛肉麵",
                        "https://www.google.com/search?q=永康牛肉麵",
                        "https://images.unsplash.com/photo-1544025162-d76694265947?auto=format&fit=crop&w=800&q=80",
                        "台北市大安區金山南路二段31巷17號"
                ),
                new AiRestaurantResponseDto(
                        "金峰魯肉飯",
                        "https://www.google.com/search?q=金峰魯肉飯",
                        "https://images.unsplash.com/photo-1552566626-52f8b828add9?auto=format&fit=crop&w=800&q=80",
                        "台北市中正區羅斯福路一段10號"
                ),
                new AiRestaurantResponseDto(
                        "林東芳牛肉麵",
                        "https://www.google.com/search?q=林東芳牛肉麵",
                        "https://images.unsplash.com/photo-1569718212165-3a8278d5f624?auto=format&fit=crop&w=800&q=80",
                        "台北市中山區八德路二段274號"
                )
        );
    }
}