package com.example.topfood2604.controller;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.service.AiRestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class AiRestaurantController {

    private final AiRestaurantService aiRestaurantService;

    public AiRestaurantController(AiRestaurantService aiRestaurantService) {
        this.aiRestaurantService = aiRestaurantService;
    }

    /**
     * AI 搜尋餐廳
     * 舊網址：/api/AiRestaurantApi/search
     * 新網址：/api/restaurants/ai/search
     */
    @GetMapping({
            "/api/AiRestaurantApi/search",
            "/api/restaurants/ai/search"
    })
    public ResponseEntity<List<AiRestaurantInfo>> aiSearchFull() {

        List<AiRestaurantInfo> list = aiRestaurantService.aiSearchFull();

        return ResponseEntity.ok(list);
    }

    /**
     * 查詢目前資料庫所有餐廳
     */
    @GetMapping({
            "/api/AiRestaurantApi/list",
            "/api/restaurants/ai/list"
    })
    public ResponseEntity<List<AiRestaurantInfo>> getRestaurants() {

        List<AiRestaurantInfo> list = aiRestaurantService.findAll();

        return ResponseEntity.ok(list);
    }

    /**
     * 清空資料庫（測試用）
     */
    @DeleteMapping({
            "/api/AiRestaurantApi/clear",
            "/api/restaurants/ai/clear"
    })
    public ResponseEntity<String> clearRestaurants() {

        aiRestaurantService.deleteAll();

        return ResponseEntity.ok("資料已清空");
    }
}