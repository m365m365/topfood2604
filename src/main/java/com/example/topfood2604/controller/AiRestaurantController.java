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
     * 不寫入資料庫
     * 直接回傳 JSON 給前端
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
     * 首頁初始載入舊資料
     * 這個可以暫時保留
     */
    @GetMapping({
            "/api/AiRestaurantApi/list",
            "/api/restaurants/ai/list"
    })
    public ResponseEntity<List<AiRestaurantInfo>> getRestaurants() {

        List<AiRestaurantInfo> list = aiRestaurantService.findAll();

        return ResponseEntity.ok(list);
    }
}