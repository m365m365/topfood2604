package com.example.topfood2604.controller;

import com.example.topfood2604.entity.RecommendedRestaurant;
import com.example.topfood2604.repository.RecommendedRestaurantRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class AdminController {

    private final RecommendedRestaurantRepository recommendedRestaurantRepository;

    public AdminController(RecommendedRestaurantRepository recommendedRestaurantRepository) {
        this.recommendedRestaurantRepository = recommendedRestaurantRepository;
    }

    @GetMapping("/admin")
    public String adminPage(Model model) {

        List<RecommendedRestaurant> restaurants =
                recommendedRestaurantRepository.findAll();

        model.addAttribute("restaurants", restaurants);

        return "admin";
    }

    @GetMapping("/admin/recommend/delete/{id}")
    public String softDeleteRecommend(@PathVariable Long id) {

        RecommendedRestaurant restaurant =
                recommendedRestaurantRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("找不到推薦餐廳 ID：" + id));

        restaurant.setStatus("DELETED");

        recommendedRestaurantRepository.save(restaurant);

        return "redirect:/admin";
    }
    @GetMapping("/admin/recommend/view/{id}")
    public String viewRecommend(@PathVariable Long id, Model model) {

        RecommendedRestaurant restaurant =
                recommendedRestaurantRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("找不到推薦餐廳 ID：" + id));

        model.addAttribute("restaurant", restaurant);

        return "admin-recommend-detail";
    }
}