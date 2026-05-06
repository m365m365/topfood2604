package com.example.topfood2604.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class MemberApiController {

    @GetMapping("/api/member/check-login")
    public Map<String, Object> checkLogin(Authentication authentication) {

        boolean loggedIn =
                authentication != null &&
                        authentication.isAuthenticated() &&
                        !"anonymousUser".equals(authentication.getPrincipal());

        return Map.of(
                "loggedIn", loggedIn
        );
    }
}