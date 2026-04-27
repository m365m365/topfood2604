package com.example.topfood2604.service;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.repository.AiRestaurantRepository;
import com.example.topfood2604.util.JsonUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
public class AiRestaurantService {

    private final ChatGptService chatGptService;
    private final GooglePlacesService googlePlacesService;
    private final AiRestaurantRepository aiRestaurantRepository;

    private static final String PROMPT =
            "請列出台北6家熱門美食餐廳，只回傳 JSON 陣列，不要加任何說明文字，不要加 markdown。"
                    + "每筆包含 name 和 url。"
                    + "格式範例：["
                    + "{\"name\":\"鼎泰豐\",\"url\":\"https://www.dintaifung.com.tw/\"},"
                    + "{\"name\":\"阜杭豆漿\",\"url\":\"https://example.com\"}"
                    + "]";

    public AiRestaurantService(ChatGptService chatGptService,
                               GooglePlacesService googlePlacesService,
                               AiRestaurantRepository aiRestaurantRepository) {
        this.chatGptService = chatGptService;
        this.googlePlacesService = googlePlacesService;
        this.aiRestaurantRepository = aiRestaurantRepository;
    }

    /**
     * 完整 AI 搜尋流程：
     * 1. 先清空舊資料
     * 2. 呼叫 GPT 取得 6 家餐廳
     * 3. 解析 JSON
     * 4. 存入 MySQL
     * 5. 呼叫 Google Places 補圖片、地址、地圖、經緯度
     * 6. 更新 MySQL
     * 7. 回傳給前端
     */
    @Transactional
    public List<AiRestaurantInfo> aiSearchFull() {

        // 1. 先清空舊資料
        aiRestaurantRepository.deleteAll();

        // 2. 呼叫 GPT
        String result = chatGptService.ask(PROMPT);

        System.out.println("=== ChatGPT 回傳 ===");
        System.out.println(result);

        // 3. JSON 轉 Java List
        List<AiRestaurantInfo> list = JsonUtil.parseRestaurantJson(result);

        // 4. 初始化資料
        for (AiRestaurantInfo restaurant : list) {
            restaurant.setId(null);
            restaurant.setState("GPT_OK");
        }

        // 5. 先存入 DB，讓每筆資料取得 id
        List<AiRestaurantInfo> savedList = aiRestaurantRepository.saveAll(list);

        // 6. 呼叫 Google Places 補資料
        for (AiRestaurantInfo restaurant : savedList) {
            try {
                Map<String, String> place =
                        googlePlacesService.findRestaurantPhoto(restaurant.getName());

                if (place.containsKey("photoUrl")) {
                    restaurant.setImageUrl(place.get("photoUrl"));
                }

                if (place.containsKey("address")) {
                    restaurant.setAddress(place.get("address"));
                }

                if (place.containsKey("mapUrl")) {
                    restaurant.setMapUrl(place.get("mapUrl"));
                }

                if (place.containsKey("embedMapUrl")) {
                    restaurant.setEmbedMapUrl(place.get("embedMapUrl"));
                }

                if (place.containsKey("lat")) {
                    restaurant.setLat(Double.valueOf(place.get("lat")));
                }

                if (place.containsKey("lng")) {
                    restaurant.setLng(Double.valueOf(place.get("lng")));
                }

                restaurant.setState("GOOGLE_OK");

            } catch (Exception e) {
                restaurant.setState("GOOGLE_ERROR");
                System.out.println("Google Places 查詢失敗：" + restaurant.getName());
                e.printStackTrace();
            }
        }

        // 7. 更新 DB，回傳前端
        return aiRestaurantRepository.saveAll(savedList);
    }

    /**
     * 查詢資料庫目前所有餐廳
     */
    public List<AiRestaurantInfo> findAll() {
        return aiRestaurantRepository.findAll();
    }


    @Transactional
    public void deleteAll() {
        aiRestaurantRepository.deleteAll();
    }
}