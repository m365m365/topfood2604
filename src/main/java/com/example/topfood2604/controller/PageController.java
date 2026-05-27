package com.example.topfood2604.controller;

import com.example.topfood2604.entity.Member;
import com.example.topfood2604.entity.MemberRecommendRestaurant;
import com.example.topfood2604.repository.MemberRepository;
import com.example.topfood2604.service.MyRecommendPageService;
import com.example.topfood2604.service.PageConfigService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class PageController {

    private final PageConfigService pageConfigService;
    private final MemberRepository memberRepository;
    private final MyRecommendPageService myRecommendPageService;

    public PageController(
            PageConfigService pageConfigService,
            MemberRepository memberRepository,
            MyRecommendPageService myRecommendPageService
    ) {
        this.pageConfigService = pageConfigService;
        this.memberRepository = memberRepository;
        this.myRecommendPageService = myRecommendPageService;
    }

    @GetMapping({"/", "/index.html"})
    public String homePage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("home"));
        return "index";
    }

    @GetMapping("/search")
    public String searchPage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("search"));
        return "search";
    }

    @GetMapping("/business")
    public String businessPage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("business"));
        return "business";
    }

    @GetMapping("/forum")
    public String forumPage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("forum"));
        return "forum";
    }

    @GetMapping("/member-center")
    public String memberCenterPage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("member-center"));
        return "member-center";
    }

    @GetMapping("/about")
    public String aboutPage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("about"));
        return "about";
    }

    @GetMapping("/recommend")
    public String recommendPage(Model model) {
        model.addAttribute("subMenus", pageConfigService.getSubMenu("recommend"));
        return "recommend";
    }


}