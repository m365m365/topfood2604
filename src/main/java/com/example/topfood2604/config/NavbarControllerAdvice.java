package com.example.topfood2604.config;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class NavbarControllerAdvice {

    @ModelAttribute
    public void addLoginInfo(Model model, Authentication authentication) {

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