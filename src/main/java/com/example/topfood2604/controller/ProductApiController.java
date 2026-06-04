package com.example.topfood2604.controller;

import com.example.topfood2604.entity.GiftProduct;
import com.example.topfood2604.repository.GiftProductRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final GiftProductRepository giftProductRepository;

    public ProductApiController(GiftProductRepository giftProductRepository) {
        this.giftProductRepository = giftProductRepository;
    }

    @GetMapping
    public List<GiftProduct> list() {
        return giftProductRepository.findByStatusOrderByCreatedAtDesc("ON");
    }
}