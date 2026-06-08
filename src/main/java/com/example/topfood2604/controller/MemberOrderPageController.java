package com.example.topfood2604.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MemberOrderPageController {

    @GetMapping("/member-orders")
    public String memberOrdersPage() {
        return "member-orders";
    }

    @GetMapping("/fake-payment/{orderId}")
    public String fakePaymentPage(
            @PathVariable Long orderId,
            Model model
    ) {
        model.addAttribute("orderId", orderId);
        return "fake-payment";
    }
}