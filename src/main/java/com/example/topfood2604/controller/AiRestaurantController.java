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
     * 首頁 AI 搜尋餐廳
     * 不寫入資料庫
     * 直接回傳 JSON 給前端
     */
    @GetMapping({
            "/api/AiRestaurantApi/search",
            "/api/restaurants/ai/search"
    })
    public ResponseEntity<List<AiRestaurantInfo>> aiSearchFull() {

        List<AiRestaurantInfo> list =
                aiRestaurantService.aiSearchFull();

        return ResponseEntity.ok(list);
    }

    /**
     * 進階 AI 搜尋餐廳
     * 專門給 search.html 使用
     *
     * 範例：
     * /api/restaurants/ai/search/advanced?city=新北市&district=板橋區&styles=約會&styles=安靜&keyword=牛排
     */
    @GetMapping("/api/restaurants/ai/search/advanced")
    public ResponseEntity<List<AiRestaurantInfo>> advancedSearch(

            @RequestParam String city,

            @RequestParam(required = false)
            String district,

            @RequestParam(required = false)
            List<String> styles,

            @RequestParam(defaultValue = "美食")
            String keyword
    ) {

        String finalKeyword =
                city + " " +
                        (district == null ? "" : district) + " " +
                        String.join(
                                " ",
                                styles == null
                                        ? List.of()
                                        : styles
                        ) + " " +
                        keyword;

        List<AiRestaurantInfo> list =
                aiRestaurantService.aiSearchFull(finalKeyword);

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

        List<AiRestaurantInfo> list =
                aiRestaurantService.findAll();

        return ResponseEntity.ok(list);
    }
}