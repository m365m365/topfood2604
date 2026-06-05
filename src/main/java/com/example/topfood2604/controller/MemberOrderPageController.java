package com.example.topfood2604.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberOrderPageController {

    @GetMapping("/member-orders")
    public String memberOrdersPage() {
        return "member-orders";
    }
}