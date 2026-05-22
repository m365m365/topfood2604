package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.entity.RestaurantReport;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.repository.RestaurantReportRepository;
import com.example.topfood2604.service.MyRecommendPageService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class PageController {

    private final MyRecommendPageService myRecommendPageService;
    private final MemberRepository memberRepository;
    private final RestaurantReportRepository restaurantReportRepository;

    public PageController(
            MyRecommendPageService myRecommendPageService,
            MemberRepository memberRepository,
            RestaurantReportRepository restaurantReportRepository
    ) {
        this.myRecommendPageService = myRecommendPageService;
        this.memberRepository = memberRepository;
        this.restaurantReportRepository = restaurantReportRepository;
    }

    /* =========================
       首頁
    ========================= */

    @GetMapping({"/", "/index.html"})
    public String index(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "index";
    }

    /* =========================
       精準尋查
    ========================= */

    @GetMapping("/search")
    public String search(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "search";
    }

    /* =========================
       商業活動
    ========================= */

    @GetMapping("/business")
    public String business(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "business";
    }

    /* =========================
       討論區
    ========================= */

    @GetMapping("/forum")
    public String forum(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "forum";
    }

    /* =========================
       會員專區
    ========================= */

    @GetMapping("/member-center")
    public String memberCenter(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "member-center";
    }

    /* =========================
       關於我
    ========================= */

    @GetMapping("/about")
    public String about(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "about";
    }

    /* =========================
       推薦頁
    ========================= */

    @GetMapping("/recommend")
    public String recommendFormPage(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);
        return "recommend";
    }

    /* =========================
       推薦詳細頁
    ========================= */

    @GetMapping("/recommend-detail")
    public String recommendDetailPage(
            @RequestParam Long id,
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);

        MemberRecommendRestaurant relation =
                myRecommendPageService.getRecommendDetail(id);

        if (relation == null) {
            return "recommend-not-found";
        }

        model.addAttribute(
                "restaurant",
                relation.getRestaurant()
        );

        return "recommend-detail";
    }

    /* =========================
       我的推薦
    ========================= */

    @GetMapping("/my-recommend")
    public String myRecommendPage(
            Model model,
            Authentication authentication
    ) {
        addLoginInfo(model, authentication);

        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            return "redirect:/login";
        }

        String username = authentication.getName();

        Member member =
                memberRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("會員不存在"));

        Long memberId = member.getId();

        List<MemberRecommendRestaurant> list =
                myRecommendPageService.getMyRestaurants(memberId);

        // 進入頁面時，先檢查每張餐廳卡片是否仍在 72 小時檢舉遮蔽中
        for (MemberRecommendRestaurant relation : list) {

            if (relation.getRestaurant() == null ||
                    relation.getRestaurant().getId() == null) {
                continue;
            }

            Optional<RestaurantReport> reportOpt =
                    restaurantReportRepository
                            .findTopByRestaurantIdAndStatusOrderByIdDesc(
                                    relation.getRestaurant().getId(),
                                    "ACTIVE"
                            );

            if (reportOpt.isPresent()) {

                RestaurantReport report = reportOpt.get();

                if (report.getBlockedUntil() != null &&
                        report.getBlockedUntil().isAfter(LocalDateTime.now())) {

                    relation.getRestaurant().setStatus("BLOCKED");
                }
            }
        }

        model.addAttribute("recommendList", list);

        return "my-recommend";
    }

    /* =========================
       共用登入資訊
    ========================= */

    private void addLoginInfo(
            Model model,
            Authentication authentication
    ) {
        boolean loggedIn =
                authentication != null &&
                        authentication.isAuthenticated() &&
                        !"anonymousUser".equals(authentication.getPrincipal());

        model.addAttribute("loggedIn", loggedIn);

        if (loggedIn) {
            model.addAttribute(
                    "loginUsername",
                    authentication.getName()
            );
        }
    }
}