package com.example.topfood2604.controller;

import com.example.topfood2604.entity.DefaultRestaurant;
import com.example.topfood2604.repository.DefaultRestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/default-restaurants")
public class DefaultRestaurantController {

    private final DefaultRestaurantRepository repository;

    public DefaultRestaurantController(DefaultRestaurantRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/ai")
    public List<DefaultRestaurant> getAiDefault() {
        return repository.findTop6ByCardAreaAndEnabledOrderBySortOrderAsc(
                "AI_DEFAULT",
                true
        );
    }

    @GetMapping("/member")
    public List<DefaultRestaurant> getMemberDefault() {
        return repository.findTop6ByCardAreaAndEnabledOrderBySortOrderAsc(
                "MEMBER_DEFAULT",
                true
        );
    }
}