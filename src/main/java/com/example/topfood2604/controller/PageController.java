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

    @GetMapping("/recommend")
    public String recommendFormPage(
            Model model,
            Authentication authentication
    ) {

        addLoginInfo(model, authentication);

        return "recommend";
    }

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
            model.addAttribute("loginUsername", authentication.getName());
        }
    }
}