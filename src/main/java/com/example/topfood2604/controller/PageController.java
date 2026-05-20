package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.service.MyRecommendPageService;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PageController {

    private final MyRecommendPageService myRecommendPageService;

    private final MemberRepository memberRepository;

    public PageController(
            MyRecommendPageService myRecommendPageService,
            MemberRepository memberRepository
    ) {

        this.myRecommendPageService = myRecommendPageService;

        this.memberRepository = memberRepository;
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

                myRecommendPageService
                        .getRecommendDetail(id);

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

        // 未登入
        if (authentication == null ||
                !authentication.isAuthenticated() ||
                "anonymousUser".equals(authentication.getPrincipal())) {

            return "redirect:/login";
        }

        // 取得登入帳號
        String username = authentication.getName();

        // 查會員
        Member member =
                memberRepository.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("會員不存在"));

        Long memberId = member.getId();

        // 查會員推薦餐廳
        List<MemberRecommendRestaurant> list =
                myRecommendPageService.getMyRestaurants(memberId);

        // 傳給 thymeleaf
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