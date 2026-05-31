package com.example.topfood2604.service;

import com.example.topfood2604.entity.AiRestaurantInfo;
import com.example.topfood2604.repository.AiRestaurantRepository;
import com.example.topfood2604.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Service
public class AiRestaurantService {

    private final ChatGptService chatGptService;
    private final GooglePlacesService googlePlacesService;
    private final AiRestaurantRepository aiRestaurantRepository;

    private static final String PROMPT =
            "請列出台北6家熱門美食餐廳，只回傳 JSON 陣列，不要加任何說明文字，不要加 markdown。"
                    + "只回傳合法 JSON array，最後一定是 ]，不能出現 )，不能加 markdown，不能加說明文字。"
                    + "每筆必須包含 name 和 url。"
                    + "url 優先使用餐廳官方網站。"
                    + "如果沒有官方網站，請使用該餐廳可公開瀏覽的介紹頁網址。"
                    + "絕對不要使用 https://example.com。"
                    + "絕對不要使用假網址。"
                    + "絕對不要使用空字串。"
                    + "格式範例：["
                    + "{\"name\":\"鼎泰豐\",\"url\":\"https://www.dintaifung.com.tw/\"},"
                    + "{\"name\":\"欣葉台菜\",\"url\":\"https://www.shinyeh.com.tw/\"}"
                    + "]";

    public AiRestaurantService(
            ChatGptService chatGptService,
            GooglePlacesService googlePlacesService,
            AiRestaurantRepository aiRestaurantRepository
    ) {
        this.chatGptService = chatGptService;
        this.googlePlacesService = googlePlacesService;
        this.aiRestaurantRepository = aiRestaurantRepository;
    }

    /**
     * 首頁 AI 搜尋流程：
     * 1. GPT 取得台北熱門餐廳 name + url
     * 2. 修正 GPT 壞網址
     * 3. Google Places 補 imageUrl / address / mapUrl / embedMapUrl / lat / lng
     * 4. 不存 MySQL，直接回傳給前端
     */
    public List<AiRestaurantInfo> aiSearchFull() {

        String result = chatGptService.ask(PROMPT);

        System.out.println("=== ChatGPT 首頁搜尋回傳 ===");
        System.out.println(result);

        List<AiRestaurantInfo> list =
                JsonUtil.parseRestaurantJson(result);

        enrichRestaurantsWithGooglePlaces(list, "台北 美食");

        return list;
    }

    /**
     * 進階 AI 搜尋流程：
     * 給 search.html 使用。
     *
     * 例如：
     * 新北市 板橋區 約會 安靜 牛排
     */
    public List<AiRestaurantInfo> aiSearchFull(String keyword) {

        if (keyword == null || keyword.trim().isEmpty()) {
            keyword = "台北 美食";
        }

        String prompt =
                "請列出「" + keyword + "」相關的6家熱門美食餐廳，只回傳 JSON 陣列，不要加任何說明文字，不要加 markdown。"
                        + "只回傳合法 JSON array，最後一定是 ]，不能出現 )，不能加 markdown，不能加說明文字。"
                        + "每筆必須包含 name 和 url。"
                        + "url 優先使用餐廳官方網站。"
                        + "如果沒有官方網站，請使用該餐廳可公開瀏覽的介紹頁網址。"
                        + "絕對不要使用 https://example.com。"
                        + "絕對不要使用假網址。"
                        + "絕對不要使用空字串。"
                        + "格式範例：["
                        + "{\"name\":\"鼎泰豐\",\"url\":\"https://www.dintaifung.com.tw/\"},"
                        + "{\"name\":\"欣葉台菜\",\"url\":\"https://www.shinyeh.com.tw/\"}"
                        + "]";

        String result = chatGptService.ask(prompt);

        System.out.println("=== ChatGPT 進階搜尋關鍵字 ===");
        System.out.println(keyword);

        System.out.println("=== ChatGPT 進階搜尋回傳 ===");
        System.out.println(result);

        List<AiRestaurantInfo> list =
                JsonUtil.parseRestaurantJson(result);

        enrichRestaurantsWithGooglePlaces(list, keyword);

        return list;
    }

    /**
     * 統一補 Google Places 資料
     */
    private void enrichRestaurantsWithGooglePlaces(
            List<AiRestaurantInfo> list,
            String searchKeyword
    ) {

        for (AiRestaurantInfo restaurant : list) {

            restaurant.setId(null);
            restaurant.setState("GPT_OK");

            if (isBadUrl(restaurant.getUrl())) {
                restaurant.setUrl(
                        buildGoogleSearchUrl(
                                searchKeyword + " " + restaurant.getName()
                        )
                );
            }

            try {
                Map<String, String> place =
                        googlePlacesService.findRestaurantPhoto(
                                searchKeyword + " " + restaurant.getName()
                        );

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

                if (isBadUrl(restaurant.getUrl())) {
                    restaurant.setUrl(
                            buildGoogleSearchUrl(
                                    searchKeyword + " " + restaurant.getName()
                            )
                    );
                }

                System.out.println("Google Places 查詢失敗：" + restaurant.getName());
                e.printStackTrace();
            }
        }
    }

    public List<AiRestaurantInfo> findAll() {
        return aiRestaurantRepository.findAll();
    }

    public void deleteAll() {
        aiRestaurantRepository.deleteAll();
    }

    private boolean isBadUrl(String url) {
        if (url == null) {
            return true;
        }

        String value = url.trim().toLowerCase();

        return value.isEmpty()
                || value.contains("example.com")
                || value.equals("#")
                || value.equals("null")
                || value.equals("n/a")
                || value.equals("無")
                || value.equals("沒有");
    }

    private String buildGoogleSearchUrl(String name) {
        String keyword = name == null ? "台北 餐廳" : name;
        String encoded =
                URLEncoder.encode(keyword, StandardCharsets.UTF_8);

        return "https://www.google.com/search?q=" + encoded;
    }
}