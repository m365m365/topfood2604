package com.example.topfood2604.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {

    @GetMapping("/recommend")
    public String recommendFormPage(Model model, Authentication authentication) {
        addLoginInfo(model, authentication);
        return "recommend";
    }

    @GetMapping("/recommend-detail")
    public String recommendDetailPage(Model model, Authentication authentication) {
        addLoginInfo(model, authentication);
        return "recommend-detail";
    }

    @GetMapping("/my-recommend")
    public String myRecommendPage(Model model, Authentication authentication) {
        addLoginInfo(model, authentication);
        return "my-recommend";
    }

    private void addLoginInfo(Model model, Authentication authentication) {
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