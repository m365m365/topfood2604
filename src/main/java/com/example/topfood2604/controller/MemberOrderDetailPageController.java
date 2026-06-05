package com.example.topfood2604.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MemberOrderDetailPageController {

    @GetMapping("/member-orders/{id}")
    public String memberOrderDetailPage(@PathVariable Long id) {
        return "member-order-detail";
    }
}