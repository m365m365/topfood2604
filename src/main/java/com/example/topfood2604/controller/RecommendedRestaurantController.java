package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.service.RecommendRestaurantService;
import com.example.topfood2604.service.S3ImageService;
import com.example.topfood2604.service.UploadLimitService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/recommended-restaurants")
@CrossOrigin
public class RecommendedRestaurantController {

    private final RecommendRestaurantService recommendRestaurantService;
    private final S3ImageService s3ImageService;
    private final UploadLimitService uploadLimitService;
    private final MemberRepository memberRepository;

    public RecommendedRestaurantController(
            RecommendRestaurantService recommendRestaurantService,
            S3ImageService s3ImageService,
            UploadLimitService uploadLimitService,
            MemberRepository memberRepository
    ) {
        this.recommendRestaurantService = recommendRestaurantService;
        this.s3ImageService = s3ImageService;
        this.uploadLimitService = uploadLimitService;
        this.memberRepository = memberRepository;
    }

    @PostMapping
    public ResponseEntity<?> createRecommend(

            @RequestParam String name,
            @RequestParam String address,
            @RequestParam Integer rating,
            @RequestParam String description,
            @RequestParam MultipartFile photo,
            Principal principal

    ) throws Exception {

        // 1. 檢查登入
        if (principal == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "請先登入"
                    )
            );
        }

        String username = principal.getName();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        Long memberId = member.getId();

        // 2. 檢查今日剩餘次數
        int remaining = uploadLimitService.getTodayRemainingCount(memberId);
        int dailyLimit = uploadLimitService.getDailyLimit(memberId);

        if (remaining <= 0) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "今日上傳次數已達上限",
                            "remaining", 0,
                            "dailyLimit", dailyLimit
                    )
            );
        }

        // 3. 檢查圖片
        if (photo == null || photo.isEmpty()) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "請選擇圖片"
                    )
            );
        }

        if (photo.getSize() > 5 * 1024 * 1024) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "圖片不可超過 5MB"
                    )
            );
        }

        String contentType = photo.getContentType();

        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/webp"))) {

            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "只允許 JPG / PNG / WEBP 圖片"
                    )
            );
        }

        // 4. 建立暫時餐廳
        RecommendedRestaurant tempRestaurant = new RecommendedRestaurant();

        tempRestaurant.setRestaurantName(name);
        tempRestaurant.setAddress(address);
        tempRestaurant.setStarRating(rating);
        tempRestaurant.setDescription(description);

        RecommendedRestaurant savedTemp =
                recommendRestaurantService.saveTemp(tempRestaurant);

        // 5. 上傳圖片到 S3
        S3ImageService.ImageResult imageResult =
                s3ImageService.uploadRestaurantImage(
                        savedTemp.getId(),
                        photo
                );

        // 6. 更新圖片 URL
        savedTemp.setImageUrl(imageResult.imageUrl());
        savedTemp.setThumbUrl(imageResult.thumbUrl());

        savedTemp.setMapUrl(
                "https://www.google.com/maps/search/?api=1&query="
                        + address
        );

        // 7. 建立會員關聯
        RecommendedRestaurant finalRestaurant =
                recommendRestaurantService.finishRecommendByUsername(
                        username,
                        savedTemp
                );

        // 8. 成功後才扣一次上傳次數
        int left = uploadLimitService.useOneUploadChance(memberId);

        // 9. 回傳 JSON
        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "message", "上傳成功，本日可上傳次數剩下 " + left + " 次",
                        "remaining", left,
                        "dailyLimit", dailyLimit

                )
        );
    }

    /**
     * 前端一進推薦頁時，可以呼叫這支 API 顯示：
     * 今日剩餘上傳次數：n / limit
     */
    @GetMapping("/upload-limit")
    public ResponseEntity<?> getUploadLimit(Principal principal) {

        if (principal == null) {
            return ResponseEntity.badRequest().body(
                    Map.of(
                            "success", false,
                            "message", "請先登入"
                    )
            );
        }

        String username = principal.getName();

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("找不到會員"));

        Long memberId = member.getId();

        int remaining = uploadLimitService.getTodayRemainingCount(memberId);
        int dailyLimit = uploadLimitService.getDailyLimit(memberId);

        return ResponseEntity.ok(
                Map.of(
                        "success", true,
                        "remaining", remaining,
                        "dailyLimit", dailyLimit
                )
        );
    }
}