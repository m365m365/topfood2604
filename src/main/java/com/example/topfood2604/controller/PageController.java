package com.example.topfood2604.controller;

import com.example.topfood2604.service.PageConfigService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    private final PageConfigService pageConfigService;

    public PageController(PageConfigService pageConfigService) {
        this.pageConfigService = pageConfigService;
    }

    // 首頁
    @GetMapping("/")
    public String homePage(Model model) {

        model.addAttribute(
                "subMenus",
                pageConfigService.getSubMenu("home")
        );

        return "index";
    }

    // 精準尋查
    @GetMapping("/search")
    public String searchPage(Model model) {

        model.addAttribute(
                "subMenus",
                pageConfigService.getSubMenu("search")
        );

        return "search";
    }

    // 商業活動
    @GetMapping("/business")
    public String businessPage(Model model) {

        model.addAttribute(
                "subMenus",
                pageConfigService.getSubMenu("business")
        );

        return "business";
    }

    // 討論區
    @GetMapping("/forum")
    public String forumPage(Model model) {

        model.addAttribute(
                "subMenus",
                pageConfigService.getSubMenu("forum")
        );

        return "forum";
    }

    // 會員專區
    @GetMapping("/member-center")
    public String memberCenterPage(Model model) {

        model.addAttribute(
                "subMenus",
                pageConfigService.getSubMenu("member-center")
        );

        return "member-center";
    }
    // 關於我
    @GetMapping("/about")
    public String aboutPage(Model model) {

        model.addAttribute(
                "subMenus",
                pageConfigService.getSubMenu("about")
        );

        return "about";
    }
}