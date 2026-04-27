package com.example.topfood2604.controller;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.service.AiRestaurantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/AiRestaurantApi")
@CrossOrigin
public class AiRestaurantController {

    private final AiRestaurantService aiRestaurantService;

    public AiRestaurantController(AiRestaurantService aiRestaurantService) {
        this.aiRestaurantService = aiRestaurantService;
    }

    /**
     * AI 搜尋餐廳
     * 呼叫 GPT → Google Places → 存入 DB → 回傳資料
     */
    @GetMapping("/search")
    public ResponseEntity<List<AiRestaurantInfo>> aiSearchFull() {

        List<AiRestaurantInfo> list = aiRestaurantService.aiSearchFull();

        return ResponseEntity.ok(list);
    }

    /**
     * 查詢目前資料庫所有餐廳
     */
    @GetMapping("/list")
    public ResponseEntity<List<AiRestaurantInfo>> getRestaurants() {

        List<AiRestaurantInfo> list = aiRestaurantService.findAll();

        return ResponseEntity.ok(list);
    }

    /**
     * 清空資料庫（測試用）
     */
    @DeleteMapping("/clear")
    public ResponseEntity<String> clearRestaurants() {

        aiRestaurantService.deleteAll();

        return ResponseEntity.ok("資料已清空");
    }

}